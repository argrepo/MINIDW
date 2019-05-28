/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.minidw.service.utils.EtlJobUtil;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.WebServiceProcessor;
import com.anvizent.minidw_druid__integration.DruidIntegration;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateStatusUpdationException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.ClassPathException;
import com.datamodel.anvizent.common.exception.ClientWebserviceRequestException;
import com.datamodel.anvizent.common.exception.FlatFileReadingException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.helper.MiniDwJobUtil;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.StreamGobbler;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.ScheduleService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.ClientServerScheduler;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.HistoricalLoadStatus;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageRunNow;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
@RestController("user_scheduleServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package/schedule")
@CrossOrigin
public class ScheduleServiceDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(ScheduleServiceDataRestController.class);


	private @Value("${alerts.thresholds.url:}") String alertsThresholdsUrl;
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ETLAdminService eTLAdminService;
	@Autowired
	private PackageDao packageDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ETLAdminDao eTLAdminDao;
	@Autowired
	WebServiceProcessor webServiceProcessor;
	
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	ExecutionProcessor executionprocessor;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;
	
	private EtlJobUtil etlJobUtil = new EtlJobUtil(Constants.Config.COMMON_ETL_JOBS, Constants.Config.ETL_JOBS,
			System.getProperty(Constants.Config.SYSTEM_PATH_SEPARATOR));

	/**
	 * get the packages of the client which have all the ILs are mapped for
	 * atleast one DL
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getPackagesForScheduling", method = RequestMethod.GET)
	public ResponseEntity<List<Package>> getPackagesForScheduling(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		List<Package> packageList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Package> userPackages = packageService.getUserPackages(clientId, clientAppDbJdbcTemplate);
			List<ClientData> scheduledPackages = scheduleService.getScheduledPackages(clientId,
					clientAppDbJdbcTemplate);
			for (Package userPackage : userPackages) {
				for (ClientData scheduledPackage : scheduledPackages) {
					if (scheduledPackage.getUserPackage().getPackageId() == userPackage.getPackageId()) {
						userPackage.setScheduleId(scheduledPackage.getSchedule().getScheduleId());
						userPackage.setScheduleStartTime(commonProcessor.getScheduleStartTime(scheduledPackage.getSchedule()));
						userPackage.setScheduleRecurrence(commonProcessor.getscheduleRecurrence(scheduledPackage.getSchedule()));
						userPackage.setScheduleRange(commonProcessor.getScheduleRange(scheduledPackage.getSchedule()));
						userPackage.setIsScheduled(true);
						userPackage.setScheduleType(scheduledPackage.getSchedule().getScheduleType());
						userPackage.setTimeZone(scheduledPackage.getSchedule().getTimeZone());
					}
				}
				packageList.add(userPackage);
			}

		} catch (AnvizentRuntimeException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return new ResponseEntity<List<Package>>(packageList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/unSchedule", method = RequestMethod.POST)
	public DataResponse unSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestBody Schedule schedule, Locale locale) {

		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		int status = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			schedule.setUserId(clientId);
			 status = scheduleService.updatePackageSchedule(schedule, clientAppDbJdbcTemplate);
			if(status == 1){
			 message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			 messages.add(message);
			 dataResponse.setMessages(messages);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				 messages.add(message);
				 dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/*public String getScheduleStartTime(Schedule schedule) {
		String scheduleStartTime = null;
		scheduleStartTime = (schedule.getScheduleStartDate() != null ? schedule.getScheduleStartDate(): "-") + " " + ( schedule.getScheduleStartTime() != null ? schedule.getScheduleStartTime():"-");

		return scheduleStartTime;

	}*/

	/*public String getscheduleRecurrence(Schedule schedule) {
		String scheduleRecurrence = null;
		if (schedule.getRecurrencePattern().equals("runnow")) {
			scheduleRecurrence = "Run Now";
		}
		if (schedule.getRecurrencePattern().equals("hourly")) {
			scheduleRecurrence = schedule.getRecurrencePattern();
		}
		if (schedule.getRecurrencePattern().equals("daily")) {
			scheduleRecurrence = schedule.getRecurrencePattern();
		}
		if (schedule.getRecurrencePattern().equals("weekly")) {
			if (schedule.getDaysToRun() != null && schedule.getWeeksToRun() != null)
				scheduleRecurrence = "On " + schedule.getDaysToRun() + " per every " + schedule.getWeeksToRun()
						+ " week(s)";
		}
		if (schedule.getRecurrencePattern().equals("monthly")) {
			if (schedule.getDayOfMonth() != null && schedule.getMonthsToRun() != null)
				scheduleRecurrence = "On " + schedule.getDayOfMonth() + " per every " + schedule.getMonthsToRun()
						+ " month(s)";
		}
		if (schedule.getRecurrencePattern().equals("yearly")) {
			if (schedule.getDayOfYear() != null && schedule.getMonthOfYear() != null
					&& schedule.getYearsToRun() != null)
				scheduleRecurrence = "On " + schedule.getDayOfYear() + " of "
						+ StringUtils.capitalize(
								Month.of(Integer.parseInt(schedule.getMonthOfYear())).toString().toLowerCase())
						+ " per every " + schedule.getYearsToRun() + " year(s)";
		}
		if (schedule.getRecurrencePattern().equals("once")) {
			scheduleRecurrence = schedule.getRecurrencePattern();
		}
		return scheduleRecurrence;

	}*/

	/*public String getScheduleRange(Schedule schedule) {
		String scheduleRange = null;
		if (schedule.getIsNoEndDate()) {
			scheduleRange = "No End Date";
		}
		if (schedule.getScheduleEndDate() != null) {
			if (schedule.getRecurrencePattern().equals("runnow")) {
				scheduleRange = schedule.getScheduleEndDate();
			} else {
				scheduleRange = "End date " + schedule.getScheduleEndDate();
			}
		}
		if (schedule.getNoOfMaxOccurences() != null) {
			scheduleRange = "Max No. of Occurence is " + schedule.getNoOfMaxOccurences();
		}
		return scheduleRange;

	}*/

	public String generateCronExpression(Schedule schedule) throws Exception {

		String dateTime = schedule.getScheduleStartTime();
		String cronTemplate = "0 {minutes} {hours} {dayofthemonth} {month} {dayoftheweek}";
		
		if (schedule.getScheduleStartDate() != null 
				&& schedule.getRecurrencePattern() != null) {
			LOGGER.info("RecurrencePattern selected" + schedule.getRecurrencePattern());
			if (schedule.getRecurrencePattern().equals("minutes")) {
				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", "0/"+schedule.getMinutesToRun());
				// 0 13 0/1 {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "*");
				// 0 13 0/1 * {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "*");
				// 0 13 0/1 * * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 0/1 * * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);
			}
			else if (schedule.getRecurrencePattern().equals("hourly")) {
				
				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				// 0 13 0/1 {dayofthemonth} {month} {dayoftheweek}
				if (StringUtils.isNotBlank(schedule.getTypeOfHours())) {
					if (schedule.getTypeOfHours().equals("every")) {
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/" + schedule.getHoursToRun());
					} else {
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", schedule.getHoursToRun());
					}
				} else {
					cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/1");
				}
				// 0 13 0/1 * {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "*");
				// 0 13 0/1 * * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 0/1 * * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			} else if (schedule.getRecurrencePattern().equals("daily")) {

				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				// 0 13 15 {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				// 0 13 15 0/1 {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "1/1");
				// 0 13 15 0/1 * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 15 0/1 * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			}

			else if (schedule.getRecurrencePattern().equals("weekly")) {

				
				// 0 09 15 ? * MONDAY,FRIDAY
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "?");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", schedule.getDaysToRun());
				
				schedule.setCronExpression(cronTemplate);

			}

			else if (schedule.getRecurrencePattern().equals("monthly")) {


				// in case if start date and day of month does not match
				int dayofmonth = Integer.parseInt(schedule.getDayOfMonth());

				// String daysTorun = schedule.getDaysToRun();
				String monthsToRun = schedule.getMonthsToRun();


				// 0 30 15 17 1/1 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", dayofmonth+"");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "1/" + monthsToRun);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");
				
				schedule.setCronExpression(cronTemplate);
			}

			else if (schedule.getRecurrencePattern().equals("yearly")) {

				// 0 30 15 17 2 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", schedule.getDayOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", schedule.getMonthOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			}
		} else {
			throw new Exception("Invalid Start date/Recurrence Pattern");
		}
		return cronTemplate;
	}


	@SuppressWarnings("unused")
	private String getWeeklyFirstOccurrenceDate(String[] weekDays, org.joda.time.LocalDate weeklyDate,
			org.joda.time.LocalDate givenDate) {
		List<org.joda.time.LocalDate> selectedDates = new ArrayList<>();
		String temp = null;
		for (String week : weekDays) {
			org.joda.time.LocalDate date = null;
			System.out.println(week);

			if (week.equalsIgnoreCase("Sunday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.SUNDAY);
			} else if (week.equalsIgnoreCase("Monday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.MONDAY);
			} else if (week.equalsIgnoreCase("Tuesday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.TUESDAY);
			} else if (week.equalsIgnoreCase("Wednesday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.WEDNESDAY);
			} else if (week.equalsIgnoreCase("Thursday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.THURSDAY);
			} else if (week.equalsIgnoreCase("Friday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.FRIDAY);
			} else if (week.equalsIgnoreCase("Saturday")) {
				date = weeklyDate.withDayOfWeek(DateTimeConstants.SATURDAY);
			}
			selectedDates.add(date);
		}
		// sorting selected recurring week days for comapring with start date.
		Collections.sort(selectedDates);

		if (selectedDates != null) {
			for (int i = 0; i < selectedDates.size(); i++) {
				int compareDates = selectedDates.get(i).compareTo(givenDate);
				if (compareDates == 0 || compareDates == 1) {
					temp = selectedDates.get(i).toString();
					break;
				}
			}
		}
		return temp;
	}

	/**
	 * @throws ParseException
	 * 
	 * 
	 */

	@RequestMapping(value = "/savePackageScheduleOld", method = RequestMethod.POST)
	public ResponseEntity<Object> savePackageSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData clientData, Locale locale,
			@RequestParam(value = "reloadUrl", required = false) String reloadUrl,
			@RequestParam(value = "jobType", required = false) String jobType, HttpServletRequest request)
			throws ParseException {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		try {
			List<Message> messages = new ArrayList<Message>();
			//boolean runOnlyDL = false;

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			/*if (jobType != null && jobType.equalsIgnoreCase("dl")) {
				runOnlyDL = true;
			}
			boolean runOnlyIL = false;
			if (jobType != null && jobType.equalsIgnoreCase("il")) {
				runOnlyIL = true;
			}*/
			message = new Message();
			clientData.setUserId(clientId);
			/*runPackage(clientIdfromHeader, clientId, clientData, message, reloadUrl, locale, request, runOnlyDL,
					runOnlyIL, clientAppDbJdbcTemplate);*/
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getMessage());
		}
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}

	/**
	 * scheduled pending packages to client scheduler
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getScheduleCurrent", method = RequestMethod.GET)
	public DataResponse getScheduleCurrent(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {

		LOGGER.debug("in getScheduleCurrent()");
		DataResponse dataResponse = new DataResponse();
		List<ClientData> scheduleCurrentList = null;
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			scheduleCurrentList = scheduleService.getScheduleCurrent(clientId, clientAppDbJdbcTemplate);
			message.setCode("SUCCESS");
			dataResponse.setObject(scheduleCurrentList);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getScheduleCurrent/{scheduleCurrentId}", method = RequestMethod.GET)
	public DataResponse getScheduleCurrentByScid(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("scheduleCurrentId") String scheduleCurrentId, HttpServletRequest request) {

		LOGGER.debug("in getScheduleCurrentByScid()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		ClientData scheduleCurrentByScid = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			scheduleCurrentByScid = scheduleService.getScheduleCurrentByScid(clientId, scheduleCurrentId,
					clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(scheduleCurrentByScid);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(ae.toString());
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * from client scheduler
	 */
	// TODO logic has to be implemented for onpremises
	@RequestMapping(value = "/updateStatusOfClientScheduler", method = RequestMethod.POST)
	public DataResponse updateStatusOfClientScheduler(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile,
			@RequestParam("packageId") Integer packageId,
			@RequestParam("currentScheduleStartTime") String currentScheduleStartTime,
			@RequestParam("currentScheduleEndTime") String currentScheduleEndTIme,
			@RequestParam("clientSchedulerStatus") String clientSchedulerStatus,
			@RequestParam("ilConnectionMappingId") Integer ilConnectionMappingId,
			@RequestParam("scheduleCurrentId") Integer scheduleCurrentId,
			@RequestParam("scheduleId") Integer scheduleId,
			@RequestParam(value = "incrementalDateValue", required = false) String incrementalDateValue,
			@RequestParam(value = "localFilePath", required = false) String localFilePath, HttpServletRequest request, Locale locale) {

		LOGGER.debug("in updateStatusOfClientScheduler()");

		String msgText = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		File tempFile = null;
		String filePath = null;

		String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
		String storageType = Constants.Config.STORAGE_TYPE_S3;
		if (StringUtils.isBlank(deploymentType)) {
			deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
		}
		if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType) && StringUtils.isBlank(localFilePath)) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.fileSourcePathNotFound",
					null, locale));
			return dataResponse;
		} else if (!Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType) && multipartFile == null) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.fileSourcePathNotFound",
					null, locale));
			return dataResponse;
		}

		if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType)) {
			storageType = Constants.Config.STORAGE_TYPE_LOCAL;
		}

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (multipartFile != null) {
				tempFile = CommonUtils.multipartToFile(multipartFile);
			} else if (StringUtils.isNotBlank(localFilePath)) {
				tempFile = new File(localFilePath);
			}

			if (tempFile != null) {

				if (localFilePath != null) {
					filePath = localFilePath;
				} else {
					S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
							clientAppDbJdbcTemplate);
					filePath = fileService.uploadFileToS3(tempFile, clientId, packageId, clientId,
							ilConnectionMappingId, request, s3BucketInfo, clientAppDbJdbcTemplate);
				}

				if (StringUtils.isNotBlank(filePath)) {
					ClientData clientData = new ClientData();
					clientData.setUserId(clientId);

					Package userPackage = new Package();
					userPackage.setPackageId(packageId);
					clientData.setUserPackage(userPackage);

					Schedule schedule = new Schedule();
					schedule.setScheduleId(scheduleId);
					schedule.setCurrentScheduleStartTime(currentScheduleStartTime);
					schedule.setCurrentScheduleEndTIme(currentScheduleEndTIme);
					clientData.setSchedule(schedule);

					ClientServerScheduler clientServerScheduler = new ClientServerScheduler();
					clientServerScheduler.setEncryptedFileName(filePath);
					clientServerScheduler.setClientSchedulerStatus(clientSchedulerStatus);
					clientServerScheduler.setId(scheduleCurrentId);
					clientData.setClientServerScheduler(clientServerScheduler);

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(ilConnectionMappingId);
					clientData.setIlConnectionMapping(ilConnectionMapping);

					Modification modification = new Modification();
					modification.setModifiedBy(Constants.Status.STATUS_CLIENT_SCHEDULER);
					modification.setModifiedDateTime(new Date());
					modification.setipAddress(SessionHelper.getIpAddress());
					clientData.setModification(modification);
					scheduleService.updateStatusOfClientScheduler(clientData, clientAppDbJdbcTemplate);
					if (StringUtils.isBlank(incrementalDateValue)) {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						msgText = "Client Scheduler status updated successfully.";
					}

					// generate next schedule date for client scheduler in case
					// of database option
					if (clientServerScheduler.getClientSchedulerStatus()
							.equalsIgnoreCase(Constants.Status.STATUS_DONE)) {
						ClientData scheduleClientData = scheduleService.getScheduleById(scheduleId,
								clientAppDbJdbcTemplate);
						scheduleClientData.getSchedule().setCurrentScheduleStartTime(currentScheduleStartTime);
						scheduleClientData.getSchedule().setCurrentScheduleEndTIme(currentScheduleEndTIme);

						String nextDate = generateNextScheduleDate(scheduleClientData.getSchedule());
						System.out.println("nextDate>>>" + nextDate);
						if (StringUtils.isNotBlank(nextDate)) {
							List<ClientData> mappingInfoList = packageService.getILConnectionMappingInfoByPackage(
									clientId, clientIdfromHeader, packageId, clientAppDbJdbcTemplate);
							for (ClientData mappingClientData : mappingInfoList) {
								ILConnectionMapping ilMapping = mappingClientData.getIlConnectionMapping();
								if (!ilMapping.getIsFlatFile()) {
									if (ilMapping.getConnectionMappingId().intValue() == clientData
											.getIlConnectionMapping().getConnectionMappingId().intValue()) {
										System.out.println("generating next date for : "
												+ clientData.getIlConnectionMapping().getConnectionMappingId());
										clientData.getSchedule().setCurrentScheduleStartTime(nextDate);
										clientData.getClientServerScheduler()
												.setClientSchedulerStatus(Constants.Status.STATUS_PENDING);
										clientData.getClientServerScheduler()
												.setServerSchedulerStatus(Constants.Status.STATUS_PENDING);
										modification.setCreatedBy(Constants.Status.STATUS_SERVER_SCHEDULER);
										modification.setCreatedTime(CommonDateHelper.formatDateAsString(new Date()));

										scheduleService.generateNextScheduleTime(clientData, clientAppDbJdbcTemplate);
									}
								}
							}
						}
					} else {
						// TODO
					}

					if (StringUtils.isNotBlank(incrementalDateValue)) {
						clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
						scheduleService.saveOrUpdateIncrementalUpdate(incrementalDateValue, ilConnectionMappingId,
								clientJdbcTemplate);
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						msgText = "Client Scheduler status updated successfully.";
					}
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					msgText = messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null,
							null);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				msgText = messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null, null);
			}
		} catch (AnvizentRuntimeException ae) {
			msgText = ae.getMessage();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
		} catch (AnvizentDuplicateStatusUpdationException ae) {
			msgText = ae.getMessage();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)) {
				if (tempFile != null) {
					tempFile.delete();
				}
			}
			if (clientJdbcTemplate != null) {
				CommonUtils.closeDataSource(clientJdbcTemplate);
			}
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		message.setText(msgText);
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageId
	 * @param ilConnectionMappingId
	 * @param scheduleCurrentId
	 * @param clientSchedularStatus
	 * @param clientSchedularStatusDetails
	 * @param scheduleEndDateTime
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateScheduleCurrentStatus", method = RequestMethod.POST)
	public DataResponse updateScheduleCurrentStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("packageId") Long packageId,
			@RequestParam("ilConnectionMappingId") Long ilConnectionMappingId,
			@RequestParam("scheduleCurrentId") Integer scheduleCurrentId,
			@RequestParam("clientSchedularStatus") String clientSchedulerStatus,
			@RequestParam("clientSchedularStatusDetails") String clientSchedularStatusDetails,
			@RequestParam("scheduleEndDateTime") String currentScheduleEndTIme, HttpServletRequest request, Locale locale) {

		LOGGER.debug("in updateScheduleCurrentStatus()");

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = getModificationObject(clientId);

			int update = scheduleService.updateScheduleCurrentStatus(scheduleCurrentId, clientId, packageId,
					ilConnectionMappingId, clientSchedulerStatus, clientSchedularStatusDetails, currentScheduleEndTIme,
					modification, clientAppDbJdbcTemplate);
			if (update > 0) {
				Integer scheduleId = 0;
				String currentScheduleStartTime = null;

				Map<String, Object> map = scheduleService.getScheduleCurrentDetailsById(Integer.parseInt(clientId),
						scheduleCurrentId, clientAppDbJdbcTemplate);
				scheduleId = (Integer) map.get("scheduleId");
				currentScheduleStartTime = (String) map.get("scheduleStartDateTime");

				ClientData scheduleClientData = scheduleService.getScheduleById(scheduleId, clientAppDbJdbcTemplate);
				scheduleClientData.getSchedule().setCurrentScheduleStartTime(currentScheduleStartTime);
				scheduleClientData.getSchedule().setCurrentScheduleEndTIme(currentScheduleEndTIme);

				String nextDate = generateNextScheduleDate(scheduleClientData.getSchedule());
				System.out.println("nextDate>>>" + nextDate);

				ClientData clientData = new ClientData();
				clientData.setUserId(clientId);

				Package userPackage = new Package();
				userPackage.setPackageId(packageId.intValue());
				clientData.setUserPackage(userPackage);

				ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
				ilConnectionMapping.setConnectionMappingId(ilConnectionMappingId.intValue());
				clientData.setIlConnectionMapping(ilConnectionMapping);

				Schedule schedule = new Schedule();
				schedule.setScheduleId(scheduleId);
				schedule.setCurrentScheduleStartTime(currentScheduleStartTime);
				clientData.setSchedule(schedule);

				Modification modification1 = new Modification();
				modification1.setipAddress(SessionHelper.getIpAddress());
				clientData.setModification(modification1);

				ClientServerScheduler clientServerScheduler = new ClientServerScheduler();
				clientData.setClientServerScheduler(clientServerScheduler);

				if (StringUtils.isNotBlank(nextDate)) {
					List<ClientData> mappingInfoList = packageService.getILConnectionMappingInfoByPackage(clientId,
							clientIdfromHeader, packageId.intValue(), clientAppDbJdbcTemplate);
					for (ClientData mappingClientData : mappingInfoList) {
						ILConnectionMapping ilMapping = mappingClientData.getIlConnectionMapping();
						if (!ilMapping.getIsFlatFile()) {
							if (ilMapping.getConnectionMappingId().intValue() == ilConnectionMappingId.intValue()) {
								System.out.println("generating next date for : " + ilConnectionMappingId.intValue());
								clientData.getSchedule().setCurrentScheduleStartTime(nextDate);
								clientData.getClientServerScheduler()
										.setClientSchedulerStatus(Constants.Status.STATUS_PENDING);
								clientData.getClientServerScheduler()
										.setServerSchedulerStatus(Constants.Status.STATUS_PENDING);
								modification1.setCreatedBy(Constants.Status.STATUS_SERVER_SCHEDULER);
								modification1.setCreatedTime(CommonDateHelper.formatDateAsString(new Date()));

								scheduleService.generateNextScheduleTime(clientData, clientAppDbJdbcTemplate);
							}
						}
					}
				}
			}

			setSuccessOrErrorMessage(update, message, locale);

		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessage(message);

		return dataResponse;
	}

	private Modification getModificationObject(String userId) {
		Modification modification = new Modification(new Date());

		modification.setCreatedBy(userId);
		modification.setModifiedDateTime(new Date());

		return modification;
	}

	private void setSuccessOrErrorMessage(int update, Message message, Locale locale) {
		if (update > 0) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.scheduleCurrentStatusUpdatedSuccessfully",
					null, locale));
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdateScheduleCurrentStatus",
					null, locale));
		}
	}

	@RequestMapping(value = "/updateHistoricalLoadExecutionStatus", method = RequestMethod.POST)
	public DataResponse updateHistoricalLoadExecutionStatus(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("historicalLoadId") Long historicalLoadId, 
			@RequestParam(value= "endDate",required=false) String endDate, 
			@RequestParam(value= "ilId",required=false) Integer ilId, 
			@RequestParam(value= "connectionId",required=false) Integer connectionId, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateHistoricalLoadExecutionStatus()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		int updatedCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			updatedCount = scheduleService.updateHistoricalLoadExecutionStatus(historicalLoadId, true,
					clientAppDbJdbcTemplate);
			if (updatedCount == 1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("");
				if (StringUtils.isNotBlank(endDate)) {
					clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
					scheduleDao.saveOrUpdateIncrementalUpdate(endDate, ilId, connectionId, clientAppDbJdbcTemplate,
							clientStagingJdbcTemplate);		
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed",
						null, locale));
			}

		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/updateHistoricalLoadRunningStatus", method = RequestMethod.POST)
	public DataResponse updateHistoricalLoadRunningStatus(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("historicalLoadId") Long historicalLoadId,
			@RequestParam("runningStatus") Boolean runningStatus, HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateHistoricalLoadRunningStatus()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		int updatedCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			LOGGER.debug("Update request for " + runningStatus + " id " + historicalLoadId);
			updatedCount = scheduleService.updateHistoricalLoadRunningStatus(runningStatus, historicalLoadId,
					clientAppDbJdbcTemplate);
			if (updatedCount == 1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList",
						null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed",
						null, locale));
			}
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/updateHistoricalLoadDataNew", method = RequestMethod.POST)
	public DataResponse updateHistoricalLoadData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "filePath", required = false) String filePath, @RequestParam("ilId") Integer ilId,
			@RequestParam("noOfRecords") Long noOfRecords, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("historicalLoadId") Integer historicalLoadId,
			@RequestParam("fileSize") long fileSize, HttpServletRequest request, Locale locale) {

		LOGGER.debug("in updateHistoricalLoadData()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
		String storageType = Constants.Config.STORAGE_TYPE_S3;

		if (StringUtils.isBlank(deploymentType)) {
			deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
		}

		if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType)) {
			storageType = Constants.Config.STORAGE_TYPE_LOCAL;
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (StringUtils.isNotBlank(filePath)) {
				try {
					LOGGER.info("(Historical load New record) start... IL Id : " + ilId);
					HistoricalLoadStatus historicalLoadStatus = new HistoricalLoadStatus();
					historicalLoadStatus.setHistoricalLoadId(historicalLoadId);
					historicalLoadStatus.setStartDate(startDate);
					historicalLoadStatus.setStorageType(storageType);
					historicalLoadStatus.setEndDate(endDate);
					historicalLoadStatus.setS3FileName(filePath);
					historicalLoadStatus.setFileSize(fileSize);
					historicalLoadStatus.setSourceRecordsCount(noOfRecords);
					historicalLoadStatus.setIlExecutionStatus(false);
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId);
					historicalLoadStatus.setModification(modification);

					HistoricalLoadForm historicalLoadingDates = packageService
							.getDatesForHistoricalLoading(historicalLoadId, clientAppDbJdbcTemplate);
					historicalLoadStatus.setIlExecutionStatus(false);
					/* Update historical load date */

					if (historicalLoadingDates != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);

						Date startDateHistory = sdf.parse(historicalLoadingDates.getHistoricalFromDate());
						Date endDateHistory = sdf.parse(historicalLoadingDates.getHistoricalToDate());

						Date lastUpdatedDate = null;
						Date calculatedToDate = null;

						if (historicalLoadingDates.getHistoricalLastUpdatedTime() != null) {
							lastUpdatedDate = sdf.parse(historicalLoadingDates.getHistoricalLastUpdatedTime());
							Calendar fromCalDate = Calendar.getInstance();
							fromCalDate.setTime(lastUpdatedDate);
							fromCalDate.add(Calendar.DATE, 1);
							lastUpdatedDate = fromCalDate.getTime();
						} else {
							lastUpdatedDate = new Date(startDateHistory.getTime());
						}

						Calendar c = Calendar.getInstance();
						c.setTime(lastUpdatedDate);
						c.add(Calendar.DATE, historicalLoadingDates.getLoadInterval() - 1);
						lastUpdatedDate = c.getTime();

						if (lastUpdatedDate.compareTo(endDateHistory) < 0) {
							calculatedToDate = new Date(lastUpdatedDate.getTime());
						} else {
							calculatedToDate = new Date(endDateHistory.getTime());
						}

						String calculatedUpdatedddTimestr = sdf.format(calculatedToDate);
						int historicalUpdatedTimeStatus = packageService.updateHistoricalUpdatedTime(
								calculatedUpdatedddTimestr, historicalLoadId, clientAppDbJdbcTemplate);
						if (historicalUpdatedTimeStatus > 0) {
							LOGGER.info("next Historical time updated to " + calculatedUpdatedddTimestr + " for "
									+ historicalLoadId);
						}
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						historicalLoadStatus.setFromDate(historicalLoadingDates.getHistoricalFromDate());
						historicalLoadStatus.setToDate(historicalLoadingDates.getHistoricalToDate());
						historicalLoadStatus.setLoadInterval(historicalLoadingDates.getLoadInterval());
						historicalLoadStatus.setLastUpdatedDate(calculatedUpdatedddTimestr);
						historicalLoadStatus.setComment("Pending");
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdateLastUpdatedDate",
								null, locale));
						historicalLoadStatus.setComment("unable to update last updated time");
					}
					packageService.updateIlHistoryStatus(historicalLoadStatus, clientAppDbJdbcTemplate);

					LOGGER.info("(Historical load New record) end... IL Id : " + ilId);

				} catch (AnvizentRuntimeException | NullPointerException ae) {
					packageService.logError(ae, request, clientAppDbJdbcTemplate);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null,
							locale));
					LOGGER.error("", ae);
				} catch (ResourceAccessException | HttpServerErrorException | DataAccessException ae) {
					packageService.logError(ae, request, clientAppDbJdbcTemplate);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToConnectClientSchema",
							null, locale));
					LOGGER.error("", ae);
				} catch (Throwable t) {
					packageService.logError(t, request, clientAppDbJdbcTemplate);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null,
							locale));
					LOGGER.error("", t);
				}

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList",
						null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateHistoricalLoadData", method = RequestMethod.POST)
	public DataResponse updateHistoricalLoadDataOld(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "filePath", required = false) String filePath, @RequestParam("ilId") Integer ilId,
			@RequestParam("noOfRecords") Long noOfRecords, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("historicalLoadId") Integer historicalLoadId,
			@RequestParam("fileSize") long fileSize, HttpServletRequest request, Locale locale) {
		
		LOGGER.debug("in updateHistoricalLoadData()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		HistoricalLoadStatus historicalLoadStatus = null;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			User user = userService.getUserDetailszd(clientId, clientAppDbJdbcTemplate);
			
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			String storageType = Constants.Config.STORAGE_TYPE_S3;

			if (StringUtils.isBlank(deploymentType)) {
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}
			if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType)) {
				storageType = Constants.Config.STORAGE_TYPE_LOCAL;
			}
			
			 S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
			 FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			 ILInfo iLInfo = packageService.getILByIdWithJobName(ilId, clientIdfromHeader, clientAppDbJdbcTemplate);
			 
			if (filePath != null) {
				
				String tempPath = Constants.Temp.getTempFileDir();
				tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
				tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
				String jobFilesPath = tempPath + "JobFiles/";
				CommonUtils.createDir(jobFilesPath);
				
				HistoricalLoadForm historicalLoadForm  = packageService.getHistoricalLoadDetailsByIdWithConnectionDetails(clientIdfromHeader, historicalLoadId, clientAppDbJdbcTemplate);
		    	DataResponse runHistoricalLoadDs =	executionprocessor.runHistoricalLoad(user,jobFilesPath,clientJdbcInstance,scheduleDao,packageDao,historicalLoadForm,deploymentType,s3BucketInfo,iLInfo,filePath,fileSettings);
			  
			  if (runHistoricalLoadDs.getHasMessages() != null && runHistoricalLoadDs.getHasMessages()) {
				if(runHistoricalLoadDs.getMessages().get(0).getCode().equals("SUCCESS")){
				    historicalLoadStatus = new HistoricalLoadStatus();
					historicalLoadStatus.setHistoricalLoadId(historicalLoadId);
					historicalLoadStatus.setStartDate(startDate);
					historicalLoadStatus.setEndDate(endDate);
					historicalLoadStatus.setS3FileName(filePath);
					historicalLoadStatus.setFileSize(fileSize);
					historicalLoadStatus.setSourceRecordsCount(noOfRecords);
					historicalLoadStatus.setIlExecutionStatus(false);
					historicalLoadStatus.setStorageType(storageType);
					
					HistoricalLoadForm historicalLoadingDates = packageService.getDatesForHistoricalLoading(historicalLoadId, clientAppDbJdbcTemplate);
					historicalLoadStatus.setIlExecutionStatus(true);
					 
					if (historicalLoadingDates != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
						Date startDateHistory = sdf.parse(historicalLoadingDates.getHistoricalFromDate());
						Date endDateHistory = sdf.parse(historicalLoadingDates.getHistoricalToDate());
						Date lastUpdatedDate = null;
						Date calculatedToDate = null;
						if (historicalLoadingDates.getHistoricalLastUpdatedTime() != null) {
							lastUpdatedDate = sdf.parse(historicalLoadingDates.getHistoricalLastUpdatedTime());
							Calendar fromCalDate = Calendar.getInstance();
							fromCalDate.setTime(lastUpdatedDate);
							fromCalDate.add(Calendar.DATE, 1);
							lastUpdatedDate = fromCalDate.getTime();
						} else {
							lastUpdatedDate = new Date(startDateHistory.getTime());
						}

						Calendar c = Calendar.getInstance();
						c.setTime(lastUpdatedDate);
						c.add(Calendar.DATE, historicalLoadingDates.getLoadInterval() - 1);
						lastUpdatedDate = c.getTime();

						if (lastUpdatedDate.compareTo(endDateHistory) < 0) {
							calculatedToDate = new Date(lastUpdatedDate.getTime());
						} else {
							calculatedToDate = new Date(endDateHistory.getTime());
						}

						String calculatedUpdatedddTimestr = sdf.format(calculatedToDate);
						int historicalUpdatedTimeStatus = packageService.updateHistoricalUpdatedTime(calculatedUpdatedddTimestr, historicalLoadId, clientAppDbJdbcTemplate);
						if (historicalUpdatedTimeStatus > 0) {
							LOGGER.info("next Historical time updated to " + calculatedUpdatedddTimestr+ " for " + historicalLoadId);
						}
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						historicalLoadStatus.setFromDate(historicalLoadingDates.getHistoricalFromDate());
						historicalLoadStatus.setToDate(historicalLoadingDates.getHistoricalToDate());
						historicalLoadStatus.setLoadInterval(historicalLoadingDates.getLoadInterval());
						historicalLoadStatus.setLastUpdatedDate(calculatedUpdatedddTimestr);
						historicalLoadStatus.setComment(runHistoricalLoadDs.getMessages().get(0).getText());
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdateLastUpdatedDate",
								null, locale));
						historicalLoadStatus.setComment("unable to update last updated time");
					}

				} else {
					historicalLoadStatus = new HistoricalLoadStatus();
					historicalLoadStatus.setHistoricalLoadId(historicalLoadId);
					historicalLoadStatus.setStartDate(startDate);
					historicalLoadStatus.setEndDate(endDate);
					historicalLoadStatus.setS3FileName(filePath);
					historicalLoadStatus.setFileSize(fileSize);
					historicalLoadStatus.setSourceRecordsCount(noOfRecords);
					historicalLoadStatus.setIlExecutionStatus(false);
					historicalLoadStatus.setStorageType(storageType);
					
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
        			message.setText(runHistoricalLoadDs.getMessages().get(0).getText() );
					historicalLoadStatus.setComment(runHistoricalLoadDs.getMessages().get(0).getText());
				}
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				historicalLoadStatus.setModification(modification);
				packageService.updateIlHistoryStatus(historicalLoadStatus, clientAppDbJdbcTemplate);
            	} 
			}
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getMessage());
		} catch (AmazonS3Exception e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getMessage());
		} catch (Throwable t) {
			LOGGER.error("", t);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	public static String generateNextScheduleDate(Schedule schedule) {

		String nextCurrentScheduleStartDateTime = null;

		String currentScheduleStartDate = schedule.getCurrentScheduleStartTime().split(" ")[0];

		String currentScheduleEndDate = schedule.getCurrentScheduleEndTIme().split(" ")[0];
		String currentScheduleEndTime = schedule.getCurrentScheduleEndTIme().split(" ")[1];

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime localScheduleEndDateTime = null;

		LocalDate localCurrentScheduleStartDate = LocalDate.parse(currentScheduleStartDate);

		LocalDate localCurrentScheduleEndDate = LocalDate.parse(currentScheduleEndDate);
		LocalTime localCurrentScheduleEndTime = LocalTime.parse(currentScheduleEndTime);

		LocalDate localScheduleStartDate = LocalDate.parse(schedule.getScheduleStartDate());
		LocalTime localScheduleStartTime = LocalTime.parse(schedule.getScheduleStartTime());
		LocalDate localScheduleEndDate = null;
		if (schedule.getScheduleEndDate() != null) {
			localScheduleEndDate = LocalDate.parse(schedule.getScheduleEndDate());
		}

		LocalDateTime localCurrentScheduleEndDateTime = LocalDateTime.of(localCurrentScheduleEndDate,
				localCurrentScheduleEndTime);
		LocalDateTime localScheduleStartDateTime = LocalDateTime.of(localScheduleStartDate, localScheduleStartTime);

		int noOfOccurence = 0;

		if (schedule.getNoOfMaxOccurences() != null) {

			noOfOccurence = Integer.parseInt(schedule.getNoOfMaxOccurences()) - 1;
		}

		// hourly
		if (schedule.getRecurrencePattern().equalsIgnoreCase("hourly")) {
			// no. of max occurrences option
			if (schedule.getNoOfMaxOccurences() != null) {
				// as first occurrence happened
				localScheduleEndDateTime = localScheduleStartDateTime.plus(noOfOccurence, ChronoUnit.HOURS);
			}
			// end date option
			else if (schedule.getScheduleEndDate() != null) {

				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);
				// as first occurrence happened
			}
			// no end date option
			else {

				// adding 1 hour to the current schedule end date time so that
				// next schedule date always generates
				localScheduleEndDateTime = localCurrentScheduleEndDateTime.plusHours(1);

			}

			if (localCurrentScheduleEndDateTime.isBefore(localScheduleEndDateTime)) {

				int minutes = localScheduleStartTime.getMinute();
				nextCurrentScheduleStartDateTime = localCurrentScheduleEndDateTime.plusHours(1)
						.truncatedTo(ChronoUnit.HOURS).plusMinutes(minutes).format(formatter);

			} else {
				// TODO max occurrence reached
			}

			// check if next schedule date is after schedule end date,if true
			// return null
			if (schedule.getScheduleEndDate() != null) {
				LocalDate localNextScheduleStartDate = LocalDate.parse(nextCurrentScheduleStartDateTime.split(" ")[0]);
				LocalTime localNextScheduleStartTime = LocalTime.parse(nextCurrentScheduleStartDateTime.split(" ")[1]);
				LocalDateTime localNextScheduleStartDateTime = LocalDateTime.of(localNextScheduleStartDate,
						localNextScheduleStartTime);
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);

				if (localNextScheduleStartDateTime.isAfter(localScheduleEndDateTime)) {
					nextCurrentScheduleStartDateTime = null;
				}
			}

		}

		// daily
		if (schedule.getRecurrencePattern().equalsIgnoreCase("daily")) {
			// no. of max occurrences option
			if (schedule.getNoOfMaxOccurences() != null) {
				// as first occurrence happened
				localScheduleEndDateTime = localScheduleStartDateTime.plus(noOfOccurence, ChronoUnit.DAYS);
			}
			// end date option
			else if (schedule.getScheduleEndDate() != null) {
				// as first occurrence happened
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);
			}
			// no end date option
			else {
				// adding 1 day to the current schedule end date time so that
				// next schedule date always generates
				localScheduleEndDateTime = localCurrentScheduleEndDateTime.plusDays(1);
			}

			if (localCurrentScheduleEndDateTime.isBefore(localScheduleEndDateTime)) {

				LocalDateTime localNextSchduleDateTime = LocalDateTime
						.of(localCurrentScheduleEndDate, localScheduleStartTime).plusDays(1);
				nextCurrentScheduleStartDateTime = localNextSchduleDateTime.format(formatter);

			} else {
				// TODO max occurrence reached
			}

			// check if next schedule date is after schedule end date,if true
			if (schedule.getScheduleEndDate() != null) {
				LocalDate localNextScheduleStartDate = LocalDate.parse(nextCurrentScheduleStartDateTime.split(" ")[0]);
				LocalTime localNextScheduleStartTime = LocalTime.parse(nextCurrentScheduleStartDateTime.split(" ")[1]);
				LocalDateTime localNextScheduleStartDateTime = LocalDateTime.of(localNextScheduleStartDate,
						localNextScheduleStartTime);
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);

				if (localNextScheduleStartDateTime.isAfter(localScheduleEndDateTime)) {
					nextCurrentScheduleStartDateTime = null;
				}
			}

		}
		// weekly
		if (schedule.getRecurrencePattern().equalsIgnoreCase("weekly")) {

			// in case of end date option
			if (schedule.getScheduleEndDate() != null) {
				if (localCurrentScheduleEndDate.isAfter(localScheduleEndDate)) {

					return nextCurrentScheduleStartDateTime;
				}
			}

			// TODO as there is clarification is required on no. of max
			// occurrences in weekly option
			String daystorun = schedule.getDaysToRun();
			int weekstorun = Integer.parseInt(schedule.getWeeksToRun());
			List<String> selectedDays = new ArrayList<>();

			if (StringUtils.isNotBlank(daystorun)) {
				if (StringUtils.contains(daystorun, ",")) {
					String[] selectedDaysArray = StringUtils.split(daystorun, ",");
					for (int i = 0; i < selectedDaysArray.length; i++) {
						selectedDays.add(selectedDaysArray[i].toUpperCase());
					}
				} else {
					selectedDays.add(daystorun.toUpperCase());
				}

				String currentScheduleEndDay = localCurrentScheduleEndDate.getDayOfWeek().name();

				WeekFields weekFields = WeekFields.ISO;
				int weekNumberCurrentScheduleEndDay = localCurrentScheduleEndDate.get(weekFields.weekOfWeekBasedYear());
				int weekNumberCurrentScheduleStartDay = localCurrentScheduleStartDate
						.get(weekFields.weekOfWeekBasedYear());

				int weekDiff = weekNumberCurrentScheduleEndDay - weekNumberCurrentScheduleStartDay;

				int remainder = weekDiff % weekstorun;
				if (remainder == 0) {

					if (selectedDays.contains(currentScheduleEndDay)) {
						int i = selectedDays.indexOf(currentScheduleEndDay);

						if (i == selectedDays.size() - 1) {

							String nextDay = selectedDays.get(0); // as it
																	// reached
																	// the last
																	// selected
																	// day
							int dayOfWeekValue = DayOfWeek.valueOf(nextDay).getValue();
							LocalDate localNextDate = localCurrentScheduleEndDate
									.with(ChronoField.DAY_OF_WEEK, dayOfWeekValue).plusWeeks(weekstorun);
							LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
									localScheduleStartTime);
							nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);

						} else {

							String nextDay = selectedDays.get(i + 1);
							int dayOfWeekValue = DayOfWeek.valueOf(nextDay).getValue();

							LocalDate localNextDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_WEEK,
									dayOfWeekValue);
							LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
									localScheduleStartTime);
							nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
						}
					} else {

						boolean isCurrentScheduleEndDayWithInTheSelectedDays = Boolean.FALSE;
						int currentScheduleEndDayOfWeekValue = DayOfWeek.valueOf(currentScheduleEndDay).getValue();

						for (String selectedDay : selectedDays) {

							int selectedDayOfWeekValue = DayOfWeek.valueOf(selectedDay).getValue();
							if (currentScheduleEndDayOfWeekValue < selectedDayOfWeekValue) {

								LocalDate localNextDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_WEEK,
										selectedDayOfWeekValue);
								LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
										localScheduleStartTime);
								nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime
										.format(formatter);
								isCurrentScheduleEndDayWithInTheSelectedDays = Boolean.TRUE;
								break;
							} else {
								isCurrentScheduleEndDayWithInTheSelectedDays = Boolean.FALSE;
							}

						}

						if (!isCurrentScheduleEndDayWithInTheSelectedDays) {

							int i = DayOfWeek.valueOf(selectedDays.get(0)).getValue();
							LocalDate localNextDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_WEEK, i)
									.plusWeeks(weekstorun);
							LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
									localScheduleStartTime);
							nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
						}

					}

				} else {

					String nextDay = selectedDays.get(0);
					int dayOfWeekValue = DayOfWeek.valueOf(nextDay).getValue();
					int weeksToAdd = weekstorun - remainder;
					LocalDate localNextDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_WEEK, dayOfWeekValue)
							.plusWeeks(weeksToAdd);
					LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
							localScheduleStartTime);
					nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);

				}
			}

			// check if next schedule date is after schedule end date,if true
			// return null
			if (schedule.getScheduleEndDate() != null) {
				LocalDate localNextScheduleStartDate = LocalDate.parse(nextCurrentScheduleStartDateTime.split(" ")[0]);
				LocalTime localNextScheduleStartTime = LocalTime.parse(nextCurrentScheduleStartDateTime.split(" ")[1]);
				LocalDateTime localNextScheduleStartDateTime = LocalDateTime.of(localNextScheduleStartDate,
						localNextScheduleStartTime);
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);

				if (localNextScheduleStartDateTime.isAfter(localScheduleEndDateTime)) {
					nextCurrentScheduleStartDateTime = null;
				}
			}
		}
		if (schedule.getRecurrencePattern().equalsIgnoreCase("monthly")) {

			if (schedule.getScheduleEndDate() != null) {

				if (localCurrentScheduleEndDate.isAfter(localScheduleEndDate)) {

					return nextCurrentScheduleStartDateTime;
				}

			}

			if (schedule.getDayOfMonth() != null && schedule.getMonthsToRun() != null) {

				int monthsToRun = Integer.parseInt(schedule.getMonthsToRun());
				int dayOfMonth = Integer.parseInt(schedule.getDayOfMonth());

				int monthValueCurrentScheduleEndDate = localCurrentScheduleEndDate.getMonthValue();
				int monthValueCurrentScheduleStartDate = localCurrentScheduleStartDate.getMonthValue();

				int diff = monthValueCurrentScheduleEndDate - monthValueCurrentScheduleStartDate;

				if (diff == 0) {
					LocalDate localNextDate = localCurrentScheduleEndDate.plusMonths(monthsToRun)
							.with(ChronoField.DAY_OF_MONTH, dayOfMonth);
					LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
							localScheduleStartTime);
					nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
				} else {

					int remainder = diff % monthsToRun;
					if (remainder == 0) {

						LocalDate localNextScheduleDate = null;
						if (dayOfMonth == 31) {

							localNextScheduleDate = localCurrentScheduleEndDate
									.with(TemporalAdjusters.lastDayOfMonth());

						} else if (dayOfMonth == 30) {
							if (monthValueCurrentScheduleEndDate == 2) {
								localNextScheduleDate = localCurrentScheduleEndDate
										.with(TemporalAdjusters.lastDayOfMonth());
							} else {
								localNextScheduleDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_MONTH,
										dayOfMonth);
							}

						} else if (dayOfMonth == 29) {
							if (monthValueCurrentScheduleEndDate == 2) {
								if (!localCurrentScheduleEndDate.isLeapYear()) {
									localNextScheduleDate = localCurrentScheduleEndDate
											.with(TemporalAdjusters.lastDayOfMonth());
								} else {
									localNextScheduleDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_MONTH,
											dayOfMonth);
								}
							} else {
								localNextScheduleDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_MONTH,
										dayOfMonth);
							}

						} else {
							localNextScheduleDate = localCurrentScheduleEndDate.with(ChronoField.DAY_OF_MONTH,
									dayOfMonth);

						}

						LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextScheduleDate,
								localScheduleStartTime);
						nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);

					} else {
						int monthsToAdd = monthsToRun - remainder;
						LocalDate localNextDate = localCurrentScheduleEndDate.plusMonths(monthsToAdd)
								.with(ChronoField.DAY_OF_MONTH, dayOfMonth);
						LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
								localScheduleStartTime);
						nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);

					}

				}

			}

			// check if next schedule date is after schedule end date,if true
			// return null
			if (schedule.getScheduleEndDate() != null) {
				LocalDate localNextScheduleStartDate = LocalDate.parse(nextCurrentScheduleStartDateTime.split(" ")[0]);
				LocalTime localNextScheduleStartTime = LocalTime.parse(nextCurrentScheduleStartDateTime.split(" ")[1]);
				LocalDateTime localNextScheduleStartDateTime = LocalDateTime.of(localNextScheduleStartDate,
						localNextScheduleStartTime);
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);

				if (localNextScheduleStartDateTime.isAfter(localScheduleEndDateTime)) {
					nextCurrentScheduleStartDateTime = null;
				}
			}
		}
		if (schedule.getRecurrencePattern().equalsIgnoreCase("yearly")) {

			if (schedule.getScheduleEndDate() != null) {

				if (localCurrentScheduleEndDate.isAfter(localScheduleEndDate)) {

					return nextCurrentScheduleStartDateTime;
				}

			}

			// TODO not considering 'years to run' option for now.

			int monthOfYear = Integer.parseInt(schedule.getMonthOfYear());
			int dayOfYear = Integer.parseInt(schedule.getDayOfYear());

			LocalDate localNextDate = localCurrentScheduleEndDate.plusYears(1);
			if (!localNextDate.isLeapYear()) {
				if (monthOfYear == 2 && dayOfYear == 29) {

					localNextDate = localNextDate.with(ChronoField.MONTH_OF_YEAR, monthOfYear)
							.with(TemporalAdjusters.lastDayOfMonth());
					LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
							localScheduleStartTime);
					nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
				} else {

					localNextDate = localNextDate.with(ChronoField.MONTH_OF_YEAR, monthOfYear)
							.with(ChronoField.DAY_OF_MONTH, dayOfYear);
					LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
							localScheduleStartTime);
					nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
				}
			} else {

				localNextDate = localNextDate.with(ChronoField.MONTH_OF_YEAR, monthOfYear)
						.with(ChronoField.DAY_OF_MONTH, dayOfYear);
				LocalDateTime localNextCurrentScheduleStartDateTime = LocalDateTime.of(localNextDate,
						localScheduleStartTime);
				nextCurrentScheduleStartDateTime = localNextCurrentScheduleStartDateTime.format(formatter);
			}

			// check if next schedule date is after schedule end date,if true
			// return null
			if (schedule.getScheduleEndDate() != null) {
				LocalDate localNextScheduleStartDate = LocalDate.parse(nextCurrentScheduleStartDateTime.split(" ")[0]);
				LocalTime localNextScheduleStartTime = LocalTime.parse(nextCurrentScheduleStartDateTime.split(" ")[1]);
				LocalDateTime localNextScheduleStartDateTime = LocalDateTime.of(localNextScheduleStartDate,
						localNextScheduleStartTime);
				localScheduleEndDateTime = LocalDateTime.of(localScheduleEndDate, localScheduleStartTime);

				if (localNextScheduleStartDateTime.isAfter(localScheduleEndDateTime)) {
					nextCurrentScheduleStartDateTime = null;
				}
			}
		}

		return nextCurrentScheduleStartDateTime;
	}

	/**
	 * to server scheduler
	 */
	@RequestMapping(value = "/getUploadedFileInfo", method = RequestMethod.GET)
	public ResponseEntity<List<ClientData>> getUploadedFileInfo(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		List<ClientData> uploadedFilesInfo = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			uploadedFilesInfo = scheduleService.getUploadedFileInfo(clientId, clientAppDbJdbcTemplate);

		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage("ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<List<ClientData>>(uploadedFilesInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/savePackagesSchedule", method = RequestMethod.POST)
	public ResponseEntity<Object> savePackagesSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<ClientData> clientDataList, Locale locale,
			@RequestParam(value = "reloadUrl", required = false) String reloadUrl, HttpServletRequest request)
			throws ParseException {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		DataResponse dataResponse = new DataResponse();
		try {

			List<Message> messages = new ArrayList<Message>();

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			/*
			 * clientDataList.forEach(clientData -> {
			 * clientData.setUserId(clientId); runPackage(clientId, clientData,
			 * message, reloadUrl, locale, request, false, false,
			 * clientAppDbJdbcTemplate); messages.add(message); });
			 */
			for (ClientData clientData : clientDataList) {
				clientData.setUserId(clientId);
				/*runPackage(clientIdfromHeader, clientId, clientData, message, reloadUrl, locale, request, false, false,
						clientAppDbJdbcTemplate);*/
				messages.add(message);
			}

			dataResponse.setMessages(messages);
		} catch (AnvizentCorewsException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(ae.toString());
		}
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/savePackageSchedule", method = RequestMethod.POST)
	public DataResponse saveScheduleInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody ClientData clientData, Locale locale,
			HttpServletRequest request) {
		
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		long packageId = clientData.getUserPackage().getPackageId();
		Integer scheduleId = clientData.getSchedule().getScheduleId();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			clientData.setUserId(userId);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> dbDetails = clientJdbcInstance.getClientDbCredentials();
			Package userPackage = packageService.getPackageById((int)packageId, userId, clientAppDbJdbcTemplate);
			clientData.setUserPackage(userPackage);
			Schedule scheduleInfo = clientData.getSchedule();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			modification.setIsActive(true);
			scheduleInfo.setModification(modification);
			clientData.setModification(modification);
			
			
			
			
			JdbcTemplate clientJdbcTemplate = null;

			try {
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			} catch (Throwable t) {
				System.out.println("");
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.clientSchemaDetailsNotFound",
						null, locale));
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
				return dataResponse;
			}
			
			if (!userPackage.getIsMapped()) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.targetTableDoesNotExistPleaseCreateATargetTableForThePackageAndSchedulePackageWillbeNotBeShownInSchedulePageWithoutATargetTable",
						null, locale).replace("?", userPackage.getPackageName()));
				return dataResponse;
			}
			if (userPackage.getIsAdvanced()) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Mappings not completed properly. Please complete");
				return dataResponse;
			}
			
			
			
			List<ClientData> schedulePackages = packageService.getILConnectionMappingInfoByPackage(userId,
					clientIdfromHeader, clientData.getUserPackage().getPackageId(), clientAppDbJdbcTemplate);
			int totalNoofsourcesProcessed = schedulePackages.size();

			if (schedulePackages == null || schedulePackages.size() == 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.package.error.text.noSourceIsAdded", null, locale));
				return dataResponse;
			}
			
			
			
			int flatFileCount = 0;
			for ( ClientData clData: schedulePackages ) {
				if ( clData.getIlConnectionMapping().getIsFlatFile() ) {
					flatFileCount++;
				}
			}
			if ( flatFileCount == totalNoofsourcesProcessed ) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				//message.setText("Schedule option is not applicable for only flat files");
				message.setText(messageSource.getMessage("anvizent.message.error.text.scheduleOptionNotApplicableforOnlyFlatFiles",
						null, locale));
				return dataResponse;
			}
			
			
			
			if (!userPackage.getIsStandard()) {
				
				List<String> derivedTablesList = new ArrayList<>();
				
				ClientData targetTableClientData = packageService.getTargetTableInfoByPackage(userId,
						clientData.getUserPackage().getPackageId(), clientAppDbJdbcTemplate);
				
				if (targetTableClientData != null) {

					Table table = targetTableClientData.getUserPackage().getTable();
					boolean isTableExist = fileService.isTableExists(null, null, table.getTableName(), clientJdbcTemplate);
					if (isTableExist) {
						derivedTablesList.add(table.getTableName());
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText("Target table does not exist at client schema");
						return dataResponse;
					}

					List<TableDerivative> tableDerivatives = fileService.getCustomTargetDerivativeTables(userId, clientData.getUserPackage().getPackageId(),
							table.getTableId(), clientAppDbJdbcTemplate);
					if (tableDerivatives != null && tableDerivatives.size() > 0) {

						for (TableDerivative tableDerivative : tableDerivatives) {
							boolean isDerivedTableExist = fileService.isTableExists(userId, null, tableDerivative.getTableName(), clientJdbcTemplate);
							if (isDerivedTableExist) {
								derivedTablesList.add(tableDerivative.getTableName());
							} else {
								message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
								message.setText("Derived table "+tableDerivative.getTableName()+" does not exist at client schema");
								return dataResponse;
							}
						}
					}

					RestTemplate restTemplate = new RestTemplate();
					if (authenticationEndPointUrl != null) {
						try {
							String encryptedClientId=AESConverter.encrypt(userId);
							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.add("X-Auth-User-Token", encryptedClientId);	
							HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(httpHeaders);

							String tableNames =String.join(",",	derivedTablesList.toArray(new String[derivedTablesList.size()]));
							ResponseEntity<?> updateStatusEntity = restTemplate.exchange(authenticationEndPointUrl + 
							"/addUserLevelTableAccessEntry/{userId}/{tableNames}/{schemaName}/{clientId}", HttpMethod.POST, headerParamsPost,   Object.class, userId, tableNames, dbDetails.get("clientdb_schema"), clientIdfromHeader);
							@SuppressWarnings("unchecked")
							Map<String, Object> updateStatus =  (Map<String, Object>)updateStatusEntity.getBody();
							System.out.println( "Updated url -- > " + authenticationEndPointUrl + "; updateStatus -- > " + updateStatus);
						} catch (Exception e) {
							packageService.logError(e, request, clientAppDbJdbcTemplate);
							LOGGER.error("", e);;
						}
					} else {
						System.out.println("error occured while reloading the table level access -- > " + authenticationEndPointUrl);
					}

				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText("Please create target table to schedule");
					return dataResponse;
				}

			}
			
			
			
			
			if ( userPackage != null ) {
				String cronExpression = generateCronExpression(scheduleInfo);
				if (StringUtils.isBlank(cronExpression)) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					//message.setText("Unable to schedule with given inputs");
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToScheduleWithGivenInputs",
							null, locale));
					return dataResponse;
				}
				
				if (clientData.getSchedule().getRecurrencePattern().equals("once")) {
					clientData.getSchedule().setTimeZone(TimeZone.getDefault().getID());
					clientData.getSchedule().setScheduleStartDate(null);
					clientData.getSchedule().setScheduleStartTime(null);
					clientData.getSchedule().setScheduleEndDate(null);
				}
				clientData.setDlInfo(new DLInfo());
				clientData.getDlInfo().setdL_id(0);
				// save or update schedule
				if (clientData.getSchedule().getScheduleId() != null && clientData.getSchedule().getScheduleId() != 0) {
					scheduleId = clientData.getSchedule().getScheduleId();
					clientData.getUserPackage().setScheduleType("Reschedule");
					scheduleService.updateSchedule(clientData, clientAppDbJdbcTemplate);
				} else {
					clientData.getUserPackage().setScheduleType("Schedule");
					scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
					clientData.getSchedule().setScheduleId((int)scheduleId);
				}
				clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
				clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
				clientData.getSchedule().setScheduleId((int)scheduleId);
				clientData.setModification(modification);
				packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.packageScheduleCreatedSuccessfully",
								new Object[] { clientData.getUserPackage().getPackageName() }, locale));
				
				// get cron expression using schedule
			}
			
		} catch (Throwable e) {
			LOGGER.error("", e);;
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		
		
		return dataResponse;
	}
	
	@RequestMapping(value="/saveRunWithSchedulerInfo", method = RequestMethod.POST)
	public DataResponse saveRunWithSchedulerInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody ClientData clientData, Locale locale,
			HttpServletRequest request){
		
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		DataResponse dataResponse = new DataResponse();
		Integer scheduleId = 0;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Package userPackage = packageService.getPackageById((int)clientData.getUserPackage().getPackageId(), userId, clientAppDbJdbcTemplate);
			
			if (!userPackage.getIsMapped()) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.targetTableDoesNotExistPleaseCreateATargetTableForThePackageAndSchedulePackageWillbeNotBeShownInSchedulePageWithoutATargetTable",
						null, locale).replace("?", userPackage.getPackageName()));
				return dataResponse;
			}
			
			
			    clientData.setUserId(userId);
				clientData.getSchedule().setScheduleStartDate(CommonDateHelper.formatDateAsString(new Date()).split(" ")[0]);
		        clientData.getSchedule().setScheduleStartTime(CommonDateHelper.formatDateAsString(new Date()).split(" ")[1]);
				clientData.getSchedule().setRecurrencePattern("once");
				clientData.getSchedule().setIsNoEndDate(false);
				clientData.getUserPackage().setScheduleType("UNSCHEDULED");
				
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(userId);
					modification.setModifiedBy(userId);
					modification.setModifiedDateTime(new Date());
					modification.setIsActive(Boolean.TRUE);
					modification.setipAddress(SessionHelper.getIpAddress());

		           clientData.setModification(modification);
		           scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
		           
				    clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
					clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
					clientData.getSchedule().setScheduleId((int)scheduleId);
					clientData.setModification(modification);
					packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.packageScheduleCreatedSuccessfully", new Object[] { clientData.getUserPackage().getPackageName() }, locale));
			}catch(Throwable t){
				t.printStackTrace();
				MinidwServiceUtil.addErrorMessage(message, t);
			}
		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getILConnectionMappingS3DetailsInfoByPackage/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<Message>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		int packageId = 0;
		if (StringUtils.isNotBlank(Package_id)) {
			packageId = Integer.parseInt(Package_id);
		} else {
			return dataResponse;
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Package userPackage = packageService.getPackageById(packageId, userId, clientAppDbJdbcTemplate);
			List<ClientData> clientDataList = scheduleService.getILConnectionMappingS3DetailsInfoByPackage(userId,
					packageId, userPackage.getIsStandard(), clientAppDbJdbcTemplate);
			if (clientDataList != null && clientDataList.size() > 0) {
				for (ClientData clientData : clientDataList) {
					ILConnectionMapping ilConnectionMapping = clientData.getIlConnectionMapping();
					String storageType = ilConnectionMapping.getStorageType();
					if (storageType == null)
						storageType = Constants.Config.STORAGE_TYPE_S3;
					if (clientData.getIlInfo().getiL_id() != 0) {
						ILInfo ilinfo = packageService.getILById(clientData.getIlInfo().getiL_id(), null,
								clientAppDbJdbcTemplate);
						clientData.setIlInfo(ilinfo);
					}
					if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)
							&& StringUtils.contains(ilConnectionMapping.getFilePath(), "/")) {
						if (!ilConnectionMapping.isMultipartEnabled()) {
							String encryptedFileName = StringUtils.substringAfterLast(ilConnectionMapping.getFilePath(),
									"/");
							String originalFileName = EncryptionServiceImpl.getInstance().decrypt(encryptedFileName);
							ilConnectionMapping.setOriginalFileName(originalFileName);
							ilConnectionMapping.setFilePath(encryptedFileName);
						} else {
							ilConnectionMapping.setOriginalFileName(ilConnectionMapping.getFilePath());
						}
					} else if (storageType.equals(Constants.Config.STORAGE_TYPE_LOCAL)) {
						String filName = FilenameUtils.getName(ilConnectionMapping.getFilePath());
						ilConnectionMapping.setOriginalFileName(filName);
						ilConnectionMapping.setFilePath(filName);
					}

				}
				dataResponse.setObject(clientDataList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null, locale));
			}
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null, locale));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getILConnectionMappingS3DetailsInfoByPackage()");
			LOGGER.error("", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	class JobRunner implements Runnable {
		private String clientId, reloadUrl, deploymentType, csvSavePath;
		private ClientData clientData;
		private String clientIdfromHeader;
		boolean runOnlyDL, runOnlyIL;
		private Message message;
		private Locale locale;

		public JobRunner(String clientId, ClientData clientData, String clientIdfromHeader, String reloadUrl,
				String deploymentType, boolean runOnlyDL, boolean runOnlyIL, Message message, Locale locale,
				String csvSavePath) {
			this.clientId = clientId;
			this.clientData = clientData;
			this.clientIdfromHeader = clientIdfromHeader;
			this.reloadUrl = reloadUrl;
			this.runOnlyDL = runOnlyDL;
			this.runOnlyIL = runOnlyIL;
			this.message = message;
			this.locale = locale;
			if (StringUtils.isBlank(csvSavePath)) {
				this.csvSavePath = Constants.Temp.getTempFileDir();
			} else {
				this.csvSavePath = csvSavePath;
			}

			if (StringUtils.isBlank(deploymentType)) {
				/**
				 * we are uploading/ downloading files from s3 if the
				 * deploymentType is not onprem so that we have added hybrid
				 * (other than onprem )
				 */
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}
			this.deploymentType = deploymentType;
		}

		@SuppressWarnings({ "null", "unused" })
		@Override
		public void run() {

			JdbcTemplate clientJdbcTemplate = null;
			JdbcTemplate clientStagingJdbcTemplate = null;
			JdbcTemplate clientAppDbJdbcTemplate = null;

			// declare and initiate package runnow pojo
			// add start data
			// declare a deails stringbuilder
			PackageRunNow packageRunNow = new PackageRunNow();

			Set<ILInfo> totalILsProcessedSet = new HashSet<ILInfo>();
			Set<DLInfo> totalDLsProcessedSet = new HashSet<DLInfo>();
			int totalNoofsourcesProcessed = 0;
			String startDateTime = null;
			String status = null;

			StringBuilder detailedInformation = new StringBuilder();
			detailedInformation.append("run method execution").append("\n");
			CustomRequest customRequest = getCustomRequest(deploymentType, clientId, clientIdfromHeader);

			try {
				status = "started";

				if (clientData.getUserId() == null) {
					clientData.setUserId(clientId);
				} else {
					clientData.setUserId(clientId);
				}
				startDateTime = CommonDateHelper.formatDateAsString(new Date());

				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				modification.setModifiedBy(clientId);
				modification.setModifiedDateTime(new Date());
				modification.setipAddress(SessionHelper.getIpAddress());
				modification.setIsActive(true);
				clientData.setModification(modification);
				Integer scheduleId = null;

				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				List<ClientData> schedulePackages = packageService.getILConnectionMappingInfoByPackage(clientId,
						clientIdfromHeader, clientData.getUserPackage().getPackageId(), clientAppDbJdbcTemplate);
				totalNoofsourcesProcessed = schedulePackages.size();
				detailedInformation.append("no of sources found : " + totalNoofsourcesProcessed).append("\n");
				S3BucketInfo s3BucketInfo = null;
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader,
						clientAppDbJdbcTemplate);

				if (schedulePackages == null || schedulePackages.size() == 0) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(
							messageSource.getMessage("anvizent.package.error.text.noSourceIsAdded", null, locale));
					detailedInformation.append(
							messageSource.getMessage("anvizent.package.error.text.noSourceIsAdded", null, locale))
							.append("\n");
					return;
				}
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				Package userPackage = packageService.getPackageById(clientData.getUserPackage().getPackageId(),
						clientId, clientAppDbJdbcTemplate);
				String packageName = userPackage.getPackageName();

				Set<String> totalTablesSet = new HashSet<>();
				clientData.getSchedule()
						.setScheduleStartDate(CommonDateHelper.formatDateAsString(new Date()).split(" ")[0]);
				clientData.getSchedule()
						.setScheduleStartTime(CommonDateHelper.formatDateAsString(new Date()).split(" ")[1]);

				clientData.getUserPackage().setPackageName(userPackage.getPackageName());

				Set<Integer> dLIds = new HashSet<Integer>();

				String schemaName = clientDbDetails.get("clientdb_schema").toString();
				// staging schema name
				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
				String databaseHost = clientDbDetails.get("region_hostname").toString();
				String databasePort = clientDbDetails.get("region_port").toString();
				String databaseUserName = clientDbDetails.get("clientdb_username").toString();
				String databasePassword = clientDbDetails.get("clientdb_password").toString();
				detailedInformation.append("Client schema details retrieved").append("\n");
				if (userPackage.getIsStandard()) {
					detailedInformation.append("start standard package execution").append("\n");
					// add the etl jars to class path from external location
					// check whether the jar file already in class path
					CommonUtils.addFilesToClassPath();

					String tempPath = Constants.Temp.getTempFileDir();

					if (tempPath.contains("\\")) {
						tempPath = tempPath.replace('\\', '/');
					}

					if (!tempPath.endsWith("/")) {
						tempPath = tempPath + "/";
					}

					// creating folder when the folder does not exits.
					String jobFilesPath = tempPath + "JobFiles/";
					CommonUtils.createDir(jobFilesPath);
					if (runOnlyDL) {
						detailedInformation.append("Running only DL's").append("\n");
					}
					if (!runOnlyIL) {
						detailedInformation.append("Running only IL's").append("\n");
					}

					for (ClientData mappingInfo : schedulePackages) {
						s3BucketInfo = userService.getS3BucketInfoById(clientIdfromHeader,
								mappingInfo.getIlConnectionMapping().getS3BucketId(), clientAppDbJdbcTemplate);
						String storageType = mappingInfo.getIlConnectionMapping().getStorageType();
						if (StringUtils.isBlank(storageType))
							storageType = Constants.Config.STORAGE_TYPE_S3;

						ILConnectionMapping iMapping = mappingInfo.getIlConnectionMapping();

						if (!runOnlyIL) {
							dLIds.add(iMapping.getdLId());
						}

						if (runOnlyDL) {
							continue;
						}

						// get the IL job status and check is IL Job already run
						// for all its sources

						ILInfo iLInfo = packageService.getILByIdWithJobName(
								mappingInfo.getIlConnectionMapping().getiLId(), clientIdfromHeader,
								clientAppDbJdbcTemplate);
						totalILsProcessedSet.add(iLInfo);
						List<Map<String, Object>> incremtalUpdateList = new ArrayList<Map<String, Object>>();
						List<String> decryptedFilepaths = null;
						String datbaseTypeName = null;
						String xlsxFilePath = null;
						String ilSourceName = mappingInfo.getIlConnectionMapping().getIlSourceName();
						// avoid already completed shared IL
						if (mappingInfo.getIlConnectionMapping().getIsFlatFile()) {

							detailedInformation.append("For FlatFile as DataSource ").append("\n");

							if (mappingInfo.getIlConnectionMapping().getFilePath() != null) {

								try {

									decryptedFilepaths = MinidwServiceUtil.downloadFilesFromS3(mappingInfo.getIlConnectionMapping().getFilePath(), clientId,deploymentType, mappingInfo.getIlConnectionMapping().isMultipartEnabled(),s3BucketInfo,mappingInfo.getIlConnectionMapping().isEncryptionRequired());
								} catch (AmazonS3Exception | OnpremFileCopyException e) {
									throw new AmazonS3Exception("File downloading failed for IL " + iLInfo.getiL_name()
											+ "<br /><b>Error Details:</b>" + e.getMessage());
								}
								datbaseTypeName = "F";

							}

						} else if (mappingInfo.getIlConnectionMapping().getIsWebservice()) {
							StringBuilder tempDisplayName = new StringBuilder();
							decryptedFilepaths = new ArrayList<>();
							User user = new User();
							user.setUserId(clientId);
							user.setClientId(clientIdfromHeader);
							user.setUserName(clientId);
							decryptedFilepaths.add(webServiceProcessor.getWsFilePath(mappingInfo, user, user.getUserId(), xlsxFilePath, 
									 user.getClientId(), incremtalUpdateList,
										s3BucketInfo, deploymentType, clientDbDetails, tempDisplayName, customRequest, fileSettings, null));
							datbaseTypeName = tempDisplayName.toString();

						} else {
							// this file path will be uploaded by client windows
							// app in case of database connection
							detailedInformation.append("For DataBase as DataSource ").append("\n");
							try {
								if (mappingInfo.getIlConnectionMapping().getFilePath() != null) {
									decryptedFilepaths = MinidwServiceUtil.downloadFilesFromS3(mappingInfo.getIlConnectionMapping().getFilePath(), clientId,deploymentType, mappingInfo.getIlConnectionMapping().isMultipartEnabled(),s3BucketInfo,mappingInfo.getIlConnectionMapping().isEncryptionRequired());
									
								}
							} catch (AmazonS3Exception | OnpremFileCopyException e) {
								throw new AmazonS3Exception("File downloading failed for IL " + iLInfo.getiL_name()
										+ "<br /><b>Error Details:</b>" + e.getMessage());
							}

							datbaseTypeName = "D"
									+ mappingInfo.getIlConnectionMapping().getiLConnection().getConnectionId();
						}
						if (StringUtils.isNotBlank(ilSourceName)) {
							datbaseTypeName = ilSourceName;
						}

						if (decryptedFilepaths != null) {

							JobExecutionInfo jobExecutionInfo = null;
							totalTablesSet.add(iLInfo.getiL_table_name());
							Map<String, String> ilContextParams = packageService.getIlContextParams(mappingInfo.getIlConnectionMapping().getiLId(), clientAppDbJdbcTemplate);
							ClientJobExecutionParameters clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);
							Map<String, String> contextParams = MinidwServiceUtil.getContextParams(ilContextParams,
									databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword,
									databaseHost, databasePort, schemaName, databaseUserName, databasePassword,
									databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword,
									datbaseTypeName, iLInfo, clientId, userPackage.getPackageId(), jobFilesPath, 0,clientJobExecutionParameters,
									mappingInfo.getIlConnectionMapping().getIsIncrementalUpdate());
							if (contextParams != null) {
								for (String filePath : decryptedFilepaths) {
									User user = new User();
									user.setUserId(clientId);
									user.setClientId(clientIdfromHeader);
									user.setUserName(clientId);
									jobExecutionInfo = MinidwServiceUtil.runILEtlJob(contextParams, iLInfo, clientId,
											userPackage.getPackageId(), filePath, modification, jobFilesPath, user);
									/*
									 * scheduleService.updateJobExecutionDetails
									 * (jobExecutionInfo,
									 * clientAppDbJdbcTemplate);
									 */
									if (jobExecutionInfo != null) {
										if (jobExecutionInfo.getStatusCode() == 0) {
											if (incremtalUpdateList.size() > 0) {
												for (Map<String, Object> incrementalMap : incremtalUpdateList) {
													try {

														ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();

														ILConnection iLConnection = new ILConnection();
														Database database = new Database();
														database.setConnector_id(
																(Integer) incrementalMap.get("wsMappingId"));
														iLConnection.setDatabase(database);
														iLConnectionMapping.setiLConnection(iLConnection);

														iLConnectionMapping
																.setTypeOfCommand(Constants.SourceType.WEBSERVICE);
														iLConnectionMapping.setIncrementalDateValue(
																incrementalMap.get("currentTime").toString());
														iLConnectionMapping
																.setiLId((Integer) incrementalMap.get("iLId"));

														Modification modification1 = new Modification(new Date());
														modification1.setCreatedBy(clientIdfromHeader);
														iLConnectionMapping.setModification(modification1);

														IncrementalUpdate incrementalUpdate = scheduleService
																.getIncrementalUpdate(iLConnectionMapping,
																		clientStagingJdbcTemplate, clientStagingSchema);

														if (StringUtils
																.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
															scheduleService.updateIncrementalUpdate(iLConnectionMapping,
																	clientStagingJdbcTemplate, clientStagingSchema);
														} else {
															scheduleService.saveIncrementalUpdate(iLConnectionMapping,
																	clientStagingJdbcTemplate, clientStagingSchema);
														}
													} catch (AnvizentRuntimeException ae) {
														packageService.logError(ae, null, clientAppDbJdbcTemplate);
														throw new AnvizentRuntimeException(ae);
													} catch (Throwable t) {
														packageService.logError(t, null, clientAppDbJdbcTemplate);
														LOGGER.error("", t);
														MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
													}
												}

											}
										} else {
											message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
											message.setText("Job Execution Failed");
											status = "error";
											return;
										}
									}

								}
							}

						}
					}
					
					// run DL Jobs
					JobExecutionInfo jobExecutionInfo = null;
					// TODO for DL execution : change the following code as IL
					ClientJobExecutionParameters clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);
					Map<String, String> dlParamsVals = MinidwServiceUtil.getDlParamsVals(databaseHost, databasePort,
							clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort,
							schemaName, databaseUserName, databasePassword, databaseHost, databasePort,
							clientStagingSchema, databaseUserName, databasePassword, clientId, userPackage,
							jobFilesPath, 0,clientJobExecutionParameters,false);
					
					for (Integer dLid : dLIds) {
						DLInfo dLInfo = packageService.getDLByIdWithJobName(dLid, clientIdfromHeader,
								clientAppDbJdbcTemplate);
						totalDLsProcessedSet.add(dLInfo);
						totalTablesSet.add(dLInfo.getdL_table_name());
						Map<String, String> dlContextParams = packageService.getDlContextParams(dLInfo.getdL_id(),
								clientAppDbJdbcTemplate);
						User user = new User();
						user.setUserId(clientId);
						user.setClientId(clientIdfromHeader);
						user.setUserName(clientId);
						jobExecutionInfo = MinidwServiceUtil.runDlEtlJob(dlContextParams, dlParamsVals, dLInfo,
								clientId, userPackage, modification, jobFilesPath, user);
						/*
						 * scheduleService.updateJobExecutionDetails(
						 * jobExecutionInfo, clientAppDbJdbcTemplate);
						 */
						LOGGER.info("DL job completed...id : " + dLid + "; DL Name :" + dLInfo.getdL_name());
						List<DLInfo> anlyDlList = packageService.getAnalyticalDLs(dLid, clientAppDbJdbcTemplate);
						for (DLInfo anly_DL : anlyDlList) {
							jobExecutionInfo = MinidwServiceUtil.runAnalyticalDlEtlJob(dlContextParams, anly_DL,
									clientId, userPackage, modification, jobFilesPath, user);
							/*
							 * scheduleService.updateJobExecutionDetails(
							 * jobExecutionInfo, clientAppDbJdbcTemplate);
							 */
							LOGGER.info("Analytical DL job completed...id : " + anly_DL.getdL_id() + "; DL Name :"
									+ anly_DL.getdL_name());
						}
					}

					/**
					 * calling MLM - delete in case all DLs success
					 */

					clientData.getSchedule().setScheduleEndDate(CommonDateHelper.formatDateAsString(new Date()));
					// save schedule
					// save or update schedule
					if (clientData.getSchedule().getScheduleId() != null) {
						scheduleId = clientData.getSchedule().getScheduleId();
						scheduleService.updateSchedule(clientData, clientAppDbJdbcTemplate);
					} else {
						scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
						clientData.getSchedule().setScheduleId(scheduleId);
					}

					// update package schedule status
					clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
					clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
					clientData.getSchedule().setScheduleId(scheduleId);
					clientData.getModification().setModifiedBy(clientId);
					modification.setModifiedDateTime(new Date());
					packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);

					// TODO success status
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.scheduleIsExcecuted",
							new Object[] { packageName }, locale));

				} else {
					// Custom Package
					detailedInformation.append("start Custom package execution").append("\n");

					ClientData targetTableClientData = packageService.getTargetTableInfoByPackage(clientId,
							clientData.getUserPackage().getPackageId(), clientAppDbJdbcTemplate);
					if (targetTableClientData != null) {
						Table table = targetTableClientData.getUserPackage().getTable();
						targetTableClientData.getUserPackage().setPackageName(userPackage.getPackageName());
						boolean isTableExist = false;

						isTableExist = packageService.isTargetTableExist(clientId, table.getTableName(),
								clientAppDbJdbcTemplate);
						if (isTableExist)
							isTableExist = fileService.isTableExists(clientId, null, table.getTableName(),
									clientJdbcTemplate);
						if (isTableExist) {

							table.setSchemaName(schemaName);
							List<Column> columns = packageService.getTableStructure(null, table.getTableName(),
									clientData.getUserPackage().getIndustry().getId(), clientId, clientJdbcTemplate);

							// get original ,alias columns from
							// tbl_target_table_alias_names temp table
							List<String> originaCols = new ArrayList<String>();
							for (Column col : columns) {
								originaCols.add(col.getColumnName());
								table.setOriginalColumnNames(originaCols);
							}

							List<Map<String, Object>> targetTableAliasCols = packageService
									.targetTableAliasColumns(table.getTableId(), clientAppDbJdbcTemplate);

							if (!targetTableAliasCols.isEmpty()) {
								for (Map<String, Object> row : targetTableAliasCols) {
									String originalColumn = (String) row.get("original_columnname");
									String aliasColumn = (String) row.get("alias_columnname");

									for (Column col : columns) {
										if (col.getColumnName().equals(aliasColumn))
											col.setColumnName(originalColumn);
										table.setColumns(columns);
									}
								}
							} else {
								table.setColumns(columns);
							}

							String filePathInS3 = null;
							List<String> decryptedFilepaths = null;
							int totalRecordsProcessed = 0;
							int totalRecordsFailed = 0;
							int totalDuplicateRecordsFound = 0;
							int totalNoOfRecords = 0;

							if (userPackage.getFilesHavingSameColumns()) {
								fileService.truncateTable(schemaName,
										targetTableClientData.getUserPackage().getTable().getTableName(),
										clientJdbcTemplate);
								for (ClientData mappingInfo : schedulePackages) {
									String fileType = null;
									String delimeter = null;
									if (!mappingInfo.getIlConnectionMapping().getIsFlatFile()) {

										if (mappingInfo.getIlConnectionMapping().getFilePath() != null) {
											try {
												decryptedFilepaths = MinidwServiceUtil.downloadFilesFromS3(mappingInfo.getIlConnectionMapping().getFilePath(), clientId,deploymentType, mappingInfo.getIlConnectionMapping().isMultipartEnabled(),s3BucketInfo,mappingInfo.getIlConnectionMapping().isEncryptionRequired());
											} catch (AmazonS3Exception | OnpremFileCopyException e) {
												throw new AmazonS3Exception(
														"File downloading failed for one of the source <br /><b>Error Details:</b>"
																+ e.getMessage());
											}
										}
									} else {
										if (mappingInfo.getIlConnectionMapping().getFilePath() != null) {
											try {
												decryptedFilepaths = MinidwServiceUtil.downloadFilesFromS3(mappingInfo.getIlConnectionMapping().getFilePath(), clientId,deploymentType, mappingInfo.getIlConnectionMapping().isMultipartEnabled(),s3BucketInfo,mappingInfo.getIlConnectionMapping().isEncryptionRequired());
											} catch (AmazonS3Exception | OnpremFileCopyException e) {
												throw new AmazonS3Exception(
														"File downloading failed for one of the source <br /><b>Error Details:</b>"
																+ e.getMessage());
											}
											fileType = mappingInfo.getIlConnectionMapping().getFileType();
											delimeter = mappingInfo.getIlConnectionMapping().getDelimeter();
										}
									}
									// process the decrypted file
									if (decryptedFilepaths != null) {
										if (StringUtils.isNotBlank(schemaName)) {
											for (String filePath : decryptedFilepaths) {
												if (!mappingInfo.getIlConnectionMapping().getIsFlatFile()) {
													fileType = StringUtils.substringAfterLast(filePath, ".");
													if (fileType != null && fileType.equals(Constants.FileType.CSV)) {
														delimeter = ",";
													}
												}
												Map<String, Object> processedData;
												try {
													processedData = fileService.processDataFromFileBatch(filePath,
															fileType, delimeter, null, targetTableClientData,
															clientStagingSchema, clientJdbcTemplate);
												} catch (FlatFileReadingException e) {
													throw e;
												}
												System.out.println("processedData>>" + processedData);
												if (processedData != null) {
													Integer successRecordsOfFile = (Integer) processedData
															.get("successRecords");
													Integer failedRecordsOfFile = (Integer) processedData
															.get("failedRecords");
													Integer duplicateRecordsOfFile = (Integer) processedData
															.get("duplicateRecords");
													Integer totalRecordsOfFile = (Integer) processedData
															.get("totalRecords");

													if (successRecordsOfFile != null) {
														totalRecordsProcessed += successRecordsOfFile.intValue();
													}
													if (failedRecordsOfFile != null) {
														totalRecordsFailed += failedRecordsOfFile.intValue();
													}
													if (duplicateRecordsOfFile != null) {
														totalDuplicateRecordsFound += duplicateRecordsOfFile.intValue();
													}
													if (totalRecordsOfFile != null) {
														totalNoOfRecords += totalRecordsOfFile.intValue();
													}
												}
											}
											/**
											 * calling MiddleLevelManager Write
											 * End Point
											 */

											// target tables directives

											List<TableDerivative> tableDerivatives = fileService
													.getCustomTargetDerivativeTables(clientId,
															clientData.getUserPackage().getPackageId(),
															table.getTableId(), clientAppDbJdbcTemplate);
											totalTablesSet.add(table.getTableName());
											if (tableDerivatives != null && tableDerivatives.size() > 0) {

												for (TableDerivative tableDerivative : tableDerivatives) {
													boolean isDerivedTableExist = fileService.isTableExists(clientId,
															null, tableDerivative.getTableName(), clientJdbcTemplate);
													if (isDerivedTableExist) {
														tableDerivative.setSchemaName(schemaName);
														fileService.truncateTable(tableDerivative.getSchemaName(),
																tableDerivative.getTableName(), clientJdbcTemplate);
														List<Column> derivedTableColumns = packageService
																.getTableStructure(null,
																		tableDerivative.getTableName(), clientData
																				.getUserPackage().getIndustry().getId(),
																		clientId, clientJdbcTemplate);
														tableDerivative.setColumns(derivedTableColumns);
														totalTablesSet.add(tableDerivative.getTableName());
														tableDerivative.setPackageName(
																clientData.getUserPackage().getPackageName());
														Map<String, Object> processedResults = fileService
																.processCustomTargetDerivativeTable(tableDerivative,
																		table.getTableName(), clientJdbcTemplate);
														fileService.updateCustomTargetDerivativeTableResults(
																tableDerivative, processedResults, modification,
																clientAppDbJdbcTemplate);

														/**
														 * calling
														 * MiddleLevelManager
														 * Write End Point
														 */

													}
												}

											}
										}
									}
								}
							} else {

								clientData.getUserPackage().setTable(table);
								for (ClientData mappingInfo : schedulePackages) {
									List<String> filePathList = null;
									ILConnectionMapping ilConnectionMappingInfo = mappingInfo.getIlConnectionMapping();

									if (ilConnectionMappingInfo.getIsHavingParentTable()) {
										ILConnectionMapping ilConnectionMappings = new ILConnectionMapping();
										ILConnection ilConnections = new ILConnection();

										StringBuffer query = new StringBuffer();
										query.append("select * from ");
										query.append(ilConnectionMappingInfo.getParent_table_name());

										ilConnectionMappings.setiLquery(query.toString());
										ilConnections.setUsername(databaseUserName);
										ilConnections.setPassword(databasePassword);
										ilConnections.setServer(databaseHost + ":" + databasePort + "/" + schemaName);
										Database db = new Database();
										db.setConnector_id(
												com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
										db.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
										db.setProtocal(
												com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
										db.setDriverName(
												com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
										ilConnections.setDatabase(db);
										ilConnectionMappings.setTypeOfCommand(Constants.QueryType.QUERY);
										ilConnectionMappings.setiLConnection(ilConnections);

										String newFilePath = CommonUtils.createFileByConncetion(ilConnectionMappings);
										boolean isEncryptionRequired = fileSettings.getFileEncryption();
										if (StringUtils.isNotBlank(newFilePath)) {
											File tempFile = new File(newFilePath);
											// upload the file to S3 for back up
											if (tempFile != null) {
												try {
													SourceFileInfo sourceFileInfo = MinidwServiceUtil
															.getS3UploadedFileInfo(s3BucketInfo, tempFile, clientId,
																	clientData.getUserPackage().getPackageId(),
																	clientId, 0, deploymentType, "", false,isEncryptionRequired);
													filePathInS3 = sourceFileInfo.getFilePath();

												} catch (AmazonS3Exception | OnpremFileCopyException e) {
													throw new AmazonS3Exception(
															"File downloading failed for one of the source <br /><b>Error Details:</b>"
																	+ e.getMessage());
												}
											}

											if (tempFile != null) {
												tempFile.delete();
											}
											// download the file from S3 as
											// input to ETL job
											if (StringUtils.isNotBlank(filePathInS3)) {
												ilConnectionMappingInfo.setFilePath(filePathInS3);
												ilConnectionMappingInfo.setDelimeter(",");
												ilConnectionMappingInfo.setFileType(Constants.FileType.CSV);
											}
											String storageType = Constants.Config.STORAGE_TYPE_S3;
											if (StringUtils.isNotBlank(deploymentType) && deploymentType
													.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
												storageType = Constants.Config.STORAGE_TYPE_LOCAL;
											}
											// updating s3 file path in
											packageService.updateFilePathForDatabaseConnection(0L,
													ilConnectionMappingInfo.getConnectionMappingId(), filePathInS3,
													storageType, s3BucketInfo.getId(), false, clientAppDbJdbcTemplate);
										}

									}

									try {
										filePathList = MinidwServiceUtil.downloadFilesFromS3(ilConnectionMappingInfo.getFilePath(), clientId,deploymentType, ilConnectionMappingInfo.isMultipartEnabled(),s3BucketInfo,mappingInfo.getIlConnectionMapping().isEncryptionRequired());
									} catch (AmazonS3Exception | OnpremFileCopyException e) {
										throw new AmazonS3Exception(
												"File downloading failed for one of the source <br /><b>Error Details:</b>"
														+ e.getMessage());
									}

									String tempTableName = fileService.getTempTableNameByMappingId(
											ilConnectionMappingInfo.getConnectionMappingId(), clientAppDbJdbcTemplate);
									Package userPackageInfo = packageService.getPackageById(
											clientData.getUserPackage().getPackageId(), clientId,
											clientAppDbJdbcTemplate);

									List<Column> tableColumns = packageService.getTableStructure(null, tempTableName,
											clientData.getUserPackage().getIndustry().getId(), clientId,
											clientStagingJdbcTemplate);

									Table tableInfo = new Table();
									tableInfo.setTableName(tempTableName);
									tableInfo.setColumns(tableColumns);
									tableInfo.setSchemaName(clientStagingSchema);

									List<String> originaColumns = new ArrayList<String>();
									for (Column column : tableColumns) {
										originaColumns.add(column.getColumnName());
										tableInfo.setOriginalColumnNames(originaColumns);
									}

									ClientData clientInfo = new ClientData();
									clientInfo.setUserPackage(new Package());
									clientInfo.getUserPackage()
											.setPackageId(clientData.getUserPackage().getPackageId());
									clientInfo.getUserPackage().setPackageName(userPackageInfo.getPackageName());
									clientInfo.getUserPackage().setTable(tableInfo);

									fileService.truncateTable(tableInfo.getSchemaName(), tableInfo.getTableName(),
											clientStagingJdbcTemplate);
									for (String filePath : filePathList) {
										Map<String, Object> results;
										try {
											results = fileService.processDataFromFileBatch(filePath,
													ilConnectionMappingInfo.getFileType(),
													ilConnectionMappingInfo.getDelimeter(), null, clientInfo,
													clientStagingSchema, clientStagingJdbcTemplate);
										} catch (FlatFileReadingException e) {
											throw e;
										}
										LOGGER.debug("execution results : " + results);

									}

									fileService.truncateTable(null, table.getTableName(), clientJdbcTemplate);

									Map<String, Object> processedData = fileService.processCustomTargetTableQuery(
											clientData, clientStagingSchema, clientJdbcTemplate);

									Integer successRecordsOfFile = (Integer) processedData.get("successRecords");
									Integer failedRecordsOfFile = (Integer) processedData.get("failedRecords");
									Integer duplicateRecordsOfFile = (Integer) processedData.get("duplicateRecords");
									Integer totalRecordsOfFile = (Integer) processedData.get("totalRecords");

									if (successRecordsOfFile != null) {
										totalRecordsProcessed += successRecordsOfFile.intValue();
									}
									if (failedRecordsOfFile != null) {
										totalRecordsFailed += failedRecordsOfFile.intValue();
									}
									if (duplicateRecordsOfFile != null) {
										totalDuplicateRecordsFound += duplicateRecordsOfFile.intValue();
									}
									if (totalRecordsOfFile != null) {
										totalNoOfRecords += totalRecordsOfFile.intValue();
									}

									/**
									 * calling MiddleLevelManager Write End
									 * Point
									 */

									// target tables directives

									totalTablesSet.add(table.getTableName());
									List<TableDerivative> tableDerivatives = fileService
											.getCustomTargetDerivativeTables(clientId,
													clientData.getUserPackage().getPackageId(), table.getTableId(),
													clientAppDbJdbcTemplate);
									if (tableDerivatives != null && tableDerivatives.size() > 0) {

										for (TableDerivative tableDerivative : tableDerivatives) {
											boolean isDerivedTableExist = fileService.isTableExists(clientId, null,
													tableDerivative.getTableName(), clientJdbcTemplate);
											if (isDerivedTableExist) {

												List<Column> derivedTableColumns = packageService.getTableStructure(
														null, tableDerivative.getTableName(),
														clientData.getUserPackage().getIndustry().getId(), clientId,
														clientJdbcTemplate);
												tableDerivative.setSchemaName(schemaName);
												tableDerivative.setColumns(derivedTableColumns);
												totalTablesSet.add(tableDerivative.getTableName());
												tableDerivative
														.setPackageName(clientData.getUserPackage().getPackageName());
												fileService.truncateTable(null, tableDerivative.getTableName(),
														clientJdbcTemplate);
												Map<String, Object> processedResults = fileService
														.processCustomTargetDerivativeTable(tableDerivative,
																table.getTableName(), clientJdbcTemplate);
												fileService.updateCustomTargetDerivativeTableResults(tableDerivative,
														processedResults, modification, clientAppDbJdbcTemplate);
												/**
												 * calling MiddleLevelManager
												 * Write End Point
												 */

											}
										}
									}

								}

							}

							/**
							 * calling MLM - delete
							 * 
							 */

							// TODO update the processed records here
							table.setIsProcessed(Boolean.TRUE);
							table.setNoOfRecordsProcessed(totalRecordsProcessed);
							table.setNoOfRecordsFailed(totalRecordsFailed);
							table.setDuplicateRecords(totalDuplicateRecordsFound);
							table.setTotalRecords(totalNoOfRecords);
							clientData.getUserPackage().setTable(table);
							packageService.updateTargetTableInfo(clientData, clientAppDbJdbcTemplate);

							clientData.getSchedule()
									.setScheduleEndDate(CommonDateHelper.formatDateAsString(new Date()));
							// save or update schedule
							if (clientData.getSchedule().getScheduleId() != null) {
								scheduleId = clientData.getSchedule().getScheduleId();
								scheduleService.updateSchedule(clientData, clientAppDbJdbcTemplate);
							} else {
								scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
								clientData.getSchedule().setScheduleId(scheduleId);
							}

							// update package schedule status
							clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
							clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
							clientData.getSchedule().setScheduleId(scheduleId);
							clientData.getModification().setModifiedBy(clientId);
							modification.setModifiedDateTime(new Date());
							packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);

							// TODO set status message

							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							message.setText(messageSource.getMessage("anvizent.message.success.text.scheduleIsExcecuted",new Object[] { packageName }, locale).replace("?", packageName));
							
							List<String> list = new ArrayList<String>(totalTablesSet);
							MiniDwJobUtil.reloadUrl(clientId, list, clientIdfromHeader,schemaName,clientAppDbJdbcTemplate,authenticationEndPointUrl);
							

						} else {

							// if target table does not exist, delete everything
							// related to the the table.
							ClientData clientData1 = packageService.getTargetTableInfoByPackage(clientId,
									userPackage.getPackageId(), clientAppDbJdbcTemplate);
							userPackage.setTable(clientData1.getUserPackage().getTable());

							Table targetTable = clientData1.getUserPackage().getTable();
							// get derived target table info
							List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
									userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
							userPackage.setDerivedTables(derivedTables);
							userPackage.setModification(modification);

							packageService.deleteCustomTablesBYPackageId(userPackage, clientId, clientJdbcTemplate,clientAppDbJdbcTemplate);
							// TODO set error message with below reson
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage(
									"anvizent.message.error.text.targetTableDoesNotExistPleaseCreateATargetTableForThePackageAndSchedulePackageWillbeNotBeShownInSchedulePageWithoutATargetTable",
									null, locale).replace("?", packageName));
						}

					} else {
						// TODO set error message with "Please create target
						// tables to proceed"
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText("Please create target tables to proceed");
						return;
					}

				}

				// get list of packages dependent on this target table
				List<String> totalTablesList = new ArrayList<>(totalTablesSet);
				if (StringUtils.isNotBlank(alertsThresholdsUrl)) {
					MiniDwJobUtil.alertsAndThreshoulds(clientIdfromHeader, totalTablesList, packageName,alertsThresholdsUrl);
				} else {
					LOGGER.info(
							"/************Alerts Api call Initiation failed because of empty URL************************/");
				}

				 druidIntegration(clientIdfromHeader, clientId, totalTablesList, clientAppDbJdbcTemplate,0,"",scheduleDao,userDao,eTLAdminDao,commonJdbcTemplate);
				 
				// fetch all dependency ddl s based on executed table list
				List<DDLayout> dDLayoutList = packageService.getDDlayoutList(clientIdfromHeader,
						new ArrayList<>(totalTablesList), clientId, clientAppDbJdbcTemplate);
				if (dDLayoutList != null && dDLayoutList.size() > 0) {
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					for (DDLayout ddlayout : dDLayoutList) {
						boolean tableExist = fileService.isTableExists(clientId, null, ddlayout.getTableName(),
								clientJdbcTemplate);
						if (tableExist) {
							int count = 0;
							String errorMessage = "";
							try {
								count = com.anvizent.minidw.service.utils.helper.CommonUtils.runDDlayoutTable(ddlayout,
										clientJdbcTemplate);
							} catch (BadSqlGrammarException e) {
								errorMessage = e.getRootCause().getMessage();
							}
							ddlayout.setRunType("run now");
							ddlayout.setModification(modification);
							ddlayout.setInsertedCount(count);
							ddlayout.setErrorMessage(errorMessage);
							packageService.updateDDlayoutTableAuditLogs(clientId, ddlayout, clientAppDbJdbcTemplate);
						}

					}
				}
				List<ClientData> dependentPackages = packageService.getDependentPackagesForScheduling(clientId,
						totalTablesList, clientAppDbJdbcTemplate);
				totalTablesSet.remove(clientId);
				if (dependentPackages != null && dependentPackages.size() > 0) {
					for (ClientData dependentPackage : dependentPackages) {
						ILConnectionMapping mapping = dependentPackage.getIlConnectionMapping();
						ClientData newClientData = new ClientData();

						Package userPackage1 = new Package();
						userPackage1.setPackageId(mapping.getPackageId());
						userPackage1.setIndustry(new Industry(dependentPackage.getUserPackage().getIndustry().getId()));

						newClientData.setUserPackage(userPackage1);
						newClientData.setUserId(clientId);

						Schedule schedule = new Schedule();
						schedule.setRecurrencePattern("runnow");
						schedule.setScheduleId(dependentPackage.getSchedule().getScheduleId());
						newClientData.setSchedule(schedule);
						JobRunner jobrunner = new JobRunner(clientId, newClientData, clientIdfromHeader, reloadUrl,
								deploymentType, runOnlyDL, runOnlyIL, message, locale, csvSavePath);
						jobrunner.run();
						LOGGER.debug("dependancy package initiated -- " + mapping.getPackageId()
								+ " dependent on target table(s) --" + mapping.getParent_table_name());
					}
				}
				status = "Completed";
			} catch (AnvizentCorewsException ae) {
				packageService.logError(ae, null, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Error Occured while connecting to client schema <br /><b>Error Details:</b> "
						+ ae.getLocalizedMessage());
				status = "error";
			} catch (ClientWebserviceRequestException | FlatFileReadingException | TalendJobNotFoundException
					| CSVConversionException | AmazonS3Exception | ClassPathException ae) {
				packageService.logError(ae, null, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(ae.getMessage());
				status = "error";
			} catch (SQLException e) {
				packageService.logError(e, null, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(e.getMessage());
				status = "error";
			} catch (Error e) {
				LOGGER.error("", e);;
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Unexpected error while running the package ");
				status = "error";
			} catch (InterruptedException e) {
				LOGGER.error("", e);;
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Unexpected error while running the package ");
				status = "error";
			} catch (IOException e) {
				LOGGER.error("", e);;
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Unexpected error while running the package ");
				status = "error";
			} catch (Exception e) {
				LOGGER.error("", e);;
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Unexpected error while running the package ");
			} catch (Throwable t) {
				LOGGER.error("", t);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Unexpected error while running the package ");
				status = "error";
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {

				// set details to
				//
				packageRunNow.setPackageID(clientData.getUserPackage().getPackageId());
				packageRunNow.setStartTime(startDateTime);
				packageRunNow.setEndTime(CommonDateHelper.formatDateAsString(new Date()));

				packageRunNow.setNoOfILsProcessed(totalILsProcessedSet.size());
				packageRunNow.setNoOfDLsProcessed(totalDLsProcessedSet.size());
				packageRunNow.setNoOfSourcesProcessed(totalNoofsourcesProcessed);

				packageRunNow.setDetailedInformation(detailedInformation.toString());
				packageRunNow.setStatus(status);

				// scheduleService.savePackageRunNowDetails(packageRunNow);

				CommonUtils.closeDataSource(clientStagingJdbcTemplate);
				CommonUtils.closeDataSource(clientJdbcTemplate);
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}

		}

	}

	class AlertsAndThreshoulds extends Thread {
		private String alertsThresholdsUrl;
		private String clientId;
		private String commaSeparatedTableNames;

		public AlertsAndThreshoulds(String alertsThresholdsUrl, String clientId, String commaSeparatedTableNames) {
			this.alertsThresholdsUrl = alertsThresholdsUrl;
			this.clientId = clientId;
			this.commaSeparatedTableNames = commaSeparatedTableNames;
		}

		@Override
		public void run() {
			try {
				runCommand(
						new String[] { "java", "-jar", CommonUtils.getEtlJobsPath() + "/threshold-check-initiator.jar",
								alertsThresholdsUrl, clientId, commaSeparatedTableNames });
			} catch (Throwable t) {
				LOGGER.info("Alerts and threshoulds execution failed", t);
			}
		}

		private String runCommand(String[] excecCommand) throws Exception {
			Process proc = Runtime.getRuntime().exec(excecCommand);
			StreamGobbler errorStreamGobbler = new StreamGobbler(proc.getErrorStream());
			StreamGobbler inputStreamGobbler = new StreamGobbler(proc.getInputStream());
			inputStreamGobbler.start();
			errorStreamGobbler.start();

			int exitStatus = proc.waitFor();
			String errorDetails = errorStreamGobbler.getOutput();
			System.out.println(errorDetails);
			if (0 != exitStatus) {
				throw new Exception(errorDetails);
			} else {
				LOGGER.info(inputStreamGobbler.getOutput() + "\n" + errorDetails);
				return inputStreamGobbler.getOutput() + "\n" + errorDetails;
			}
		}
	}

	@RequestMapping(value = "/uploadFileIntoCurrencyLoad", method = RequestMethod.POST)
	public DataResponse uploadFileIntoCurrencyLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			Locale locale, HttpServletRequest request, @RequestParam(value ="flatFileType") String flatFileType,
			@RequestParam(required = false, value="file") MultipartFile multipartFile,
			@RequestParam(value ="delimeter") String delimeter,
			@RequestParam(required = false, value = "localFilePath", defaultValue ="localFilePath") String localFilePath) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String filePath = null;
			String jobFilesPath = null;
			File tempFile = null;
			String uploadedS3Path = null;
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			// for error_log_path and bulk_path
			String tempPath = Constants.Temp.getTempFileDir();
			if (tempPath.contains("\\")) {
				tempPath = tempPath.replace('\\', '/');
			}
			if (!tempPath.endsWith("/")) {
				tempPath = tempPath + "/";
			}
			jobFilesPath = tempPath + "CurrencyLoad/";

			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			String storageType = Constants.Config.STORAGE_TYPE_S3;
			if (StringUtils.isBlank(deploymentType)) {
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}
			if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType) && StringUtils.isBlank(localFilePath)) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("File source/path not found");
				return dataResponse;
			} else if (!Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType) && multipartFile == null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("File source/path not found");
				return dataResponse;
			}

			if (Constants.Config.DEPLOYMENT_TYPE_ONPREM.equals(deploymentType)) {
				storageType = Constants.Config.STORAGE_TYPE_LOCAL;
			}
			if (multipartFile != null) {
				tempFile = CommonUtils.multipartToFile(multipartFile);
			} else if (StringUtils.isNotBlank(localFilePath)) {
				tempFile = new File(localFilePath);
			}
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (tempFile != null) {
				// uploading file to s3 for future reference
				filePath = tempFile.getAbsolutePath();
				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
				boolean isEncryptionRequired = fileSettings.getFileEncryption();
				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFile, userId, 0, userId, 0, deploymentType, "", false,isEncryptionRequired);
				uploadedS3Path = sourceFileInfo.getFilePath();

			}
			CommonUtils.createDir(jobFilesPath);
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String databaseHost = clientDbDetails.get("region_hostname").toString();
			String databasePort = clientDbDetails.get("region_port").toString();
			String schemaName = clientDbDetails.get("clientdb_schema").toString();
			String clientdbUsername = clientDbDetails.get("clientdb_username").toString();
			String clientDbPassword = clientDbDetails.get("clientdb_password").toString();

			Map<String, String> ilContextParams = packageService.getIlContextParams(1, clientAppDbJdbcTemplate);
			Map<String, String> ilParamsVals = new LinkedHashMap<>();

			String clientStagingSchema = schemaName + "_staging";
			// staging properties
			ilParamsVals.put("src_host", databaseHost);
			ilParamsVals.put("src_port", databasePort);
			ilParamsVals.put("src_database", clientStagingSchema);
			ilParamsVals.put("src_user", clientdbUsername);
			ilParamsVals.put("src_pass", clientDbPassword);

			// target database properties
			ilParamsVals.put("tgt_host", databaseHost);
			ilParamsVals.put("tgt_port", databasePort);
			ilParamsVals.put("tgt_database", schemaName);
			ilParamsVals.put("tgt_user", clientdbUsername);
			ilParamsVals.put("tgt_pass", clientDbPassword);

			// master database properties

			ilParamsVals.put("master_host", databaseHost);
			ilParamsVals.put("master_port", databasePort);
			ilParamsVals.put("master_database", clientStagingSchema);
			ilParamsVals.put("master_user", clientdbUsername);
			ilParamsVals.put("master_pass", clientDbPassword);

			// staging table and target table
			ilParamsVals.put("tgt_table", "IL_Currency_Rate");
			ilParamsVals.put("stg_table", "IL_Currency_Rate_Stg");

			// client id and package id
			ilParamsVals.put("client_id", clientIdfromHeader);
			ilParamsVals.put("package_id", userId);

			// file path details
			ilParamsVals.put("src_file_path", filePath);
			ilParamsVals.put("error_log_path", jobFilesPath);
			ilParamsVals.put("bulk_path",jobFilesPath);

			ilParamsVals.put("start_date_time", CommonUtils.currentDateTime());
			ilParamsVals.put("job_name", "IL_Currency_Rate");
			ilParamsVals.put("job_type", "IL_Currency_Rate");

			ilParamsVals.put("datasourcename", "unknown");

			packageService.parseContextParams(ilContextParams, ilParamsVals);

			String[] ilContextParamsArr = CommonUtils.convertToContextParamsArray(ilContextParams);

			CurrencyIntegration currencyIntegration = eTLAdminService.getCurrencyIntegration(commonJdbcTemplate);
			if (StringUtils.isNotBlank(currencyIntegration.getJobName())) {
				int ilStatus = -1;
				ETLjobExecutionMessages etlJjobExecutionMessages = new ETLjobExecutionMessages();
				try {
					etlJjobExecutionMessages = etlJobUtil.runETLjar(currencyIntegration.getClientSpecificJobName(),
							currencyIntegration.getClientSpecificJobfile_names(), ilContextParamsArr);
					ilStatus = etlJjobExecutionMessages.getStatus();
					String executionmessages = etlJjobExecutionMessages.getErrorStreamMsg()
							+ etlJjobExecutionMessages.getInputStreamMsg();
					
					commonProcessor.moveErrorLogFile(ilContextParams.get("CLIENT_ID"), ilContextParams.get("PACKAGE_ID"),
							ilContextParams.get("JOB_NAME"), ilContextParams.get("JOB_STARTDATETIME"),
							ilContextParams.get("ETL_JOB_ERROR_LOG"), ilContextParams.get("FILE_PATH"));
					
					JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
					jobExecutionInfo.setStatusCode(ilStatus);
					jobExecutionInfo.setJobName("IL_Currency_Rate");
					jobExecutionInfo.setJobClass(currencyIntegration.getJobName());
					jobExecutionInfo.setDependencyJars(currencyIntegration.getJobfile_names());
					jobExecutionInfo.setJobId("IL_Currency_Rate_" + CommonUtils.currentDateTime());
					jobExecutionInfo.setExecutionMessages(executionmessages);
					jobExecutionInfo.setS3Path(uploadedS3Path);
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(userId);
					jobExecutionInfo.setModification(modification);
					/*
					 * scheduleService.updateJobExecutionDetails(
					 * jobExecutionInfo, clientAppDbJdbcTemplate);
					 */
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.package.success.currencyLoadJobIsExecuted",
							null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);

				} catch (InterruptedException | IOException e) {
					throw new TalendJobNotFoundException(messageSource.getMessage("anvizent.package.error.iLCurrencyLoadJobFailed",
							null, locale),
							ilStatus + e.getMessage(), e);
				} finally {
					if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)) {
						if (tempFile != null) {
							tempFile.delete();
						}
					}
				}
			}
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText("Unable to run curency load job.");
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/getJobResults", method = RequestMethod.GET)
	public DataResponse getJobResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = scheduleService.getJobResults(clientIdfromHeader, userId, "", clientJdbcTemplate);
			dataResponse.setObject(resultlist);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsByDate/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = scheduleService.getJobResultsByDate(clientIdfromHeader, userId, null, fromDate, toDate,
					clientJdbcTemplate);

		} catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/savePackageExectionInfo", method = RequestMethod.POST)
	public DataResponse savePackageExectionInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		LOGGER.debug("in savePackageExectionInfo");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int saveExecutionId = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			packageExecution.setModification(modification);
			saveExecutionId = scheduleService.savePackageExectionInfo(packageExecution, clientAppDbJdbcTemplate);
			if (saveExecutionId != 0) {
				dataResponse.setObject(saveExecutionId);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while savePackageExectionInfo() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/wsUploadExecutor", method = RequestMethod.POST)
	public DataResponse getWsFilePath(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData mappingInfo, Locale locale, HttpServletRequest request) {
		LOGGER.debug("in getWsFilePath");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		Map<String, Object> responseMap = new HashMap<>();
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

		    packageService.getILByIdWithJobName(mappingInfo.getIlConnectionMapping().getiLId(),
					clientIdfromHeader, clientAppDbJdbcTemplate);
			List<Map<String, Object>> incremtalUpdateList = new ArrayList<Map<String, Object>>();
			String xlsxFilePath = null;
			//String ilSourceName = mappingInfo.getIlConnectionMapping().getIlSourceName();
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			StringBuilder tempDisplayName = new StringBuilder();
			S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
					clientAppDbJdbcTemplate);
			if (s3BucketInfo == null) {
				s3BucketInfo = new S3BucketInfo();
			}
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			/*Package userPackage = packageService.getPackageById(mappingInfo.getUserPackage().getPackageId(), mappingInfo.getUserPackage().getPackageId() == 0 ? clientId,
					clientAppDbJdbcTemplate);*/
			Package userPackage = mappingInfo.getUserPackage();
			Schedule shedule = new Schedule();
			shedule.setScheduleStartDate(CommonDateHelper.formatDateAsString(new Date()).split(" ")[0]);
			mappingInfo.setSchedule(shedule);
			mappingInfo.setUserPackage(userPackage);

			User user = new User();
			user.setUserId(clientId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			tempDisplayName.append(mappingInfo.getIlConnectionMapping().getIlSourceName());
			
			CustomRequest customRequest = getCustomRequest(deploymentType, clientId, clientIdfromHeader);
			FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			PackageExecution packageExecution = new PackageExecution();
			packageExecution.setExecutionId(mappingInfo.getIlConnectionMapping().getPackageExecutionId());
			
			
			
			String filePath = webServiceProcessor.getWsFilePath(mappingInfo, user, clientId, xlsxFilePath, 
					 user.getClientId(), incremtalUpdateList,
					s3BucketInfo, deploymentType, clientDbDetails, tempDisplayName, customRequest, fileSettings, packageExecution);
			if (filePath != null) {
				DataResponse  wsUploadExecutorDs = webServiceProcessor.wsUploadExecutor(mappingInfo,user, clientId, userPackage,filePath,s3BucketInfo, customRequest, fileSettings, null);
				if(wsUploadExecutorDs != null && wsUploadExecutorDs.getHasMessages() ){
					if(wsUploadExecutorDs.getMessages().get(0).getCode().equals("SUCCESS")){
						message.setCode(wsUploadExecutorDs.getMessages().get(0).getCode());
						responseMap.put("incremtalUpdateList", incremtalUpdateList);
						dataResponse.setObject(responseMap);
					}else{ 
						message.setCode(wsUploadExecutorDs.getMessages().get(0).getCode());
						message.setText(wsUploadExecutorDs.getMessages().get(0).getText());
					}
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("unable to get upload file.");
			}
		}catch(HttpClientErrorException e){
			LOGGER.error("error while getWsFilePath() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getWsFilePath() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCronExpression", method = RequestMethod.GET)
	public DataResponse getCronExpression(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		List<Schedule> schedule = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			schedule = scheduleService.getPackagesForSchedulingByClientIdWithCronExpression(clientId,
					clientAppDbJdbcTemplate);
			if (schedule != null) {
				dataResponse.setObject(schedule);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetCronExpression", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getCronExpression() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetCronExpression", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/alertsAndThreshoulds", method = RequestMethod.POST)
	public DataResponse alertsAndThreshoulds(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Package userPackage = packageService.getPackageById((int) packageExecution.getPackageId(), userId,clientAppDbJdbcTemplate);
			MiniDwJobUtil.alertsAndThreshoulds(clientIdfromHeader, packageExecution.getDerivedTablesList(),userPackage.getPackageName(),alertsThresholdsUrl);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Alerts And Threshoulds executed successfully.");
		} catch (Throwable ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while alertsAndThreshoulds() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/druidIntegration", method = RequestMethod.POST)
	public DataResponse druidIntegration(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			druidIntegration(clientIdfromHeader, userId, packageExecution.getDerivedTablesList(),clientAppDbJdbcTemplate,packageExecution.getExecutionId(),
					packageExecution.getTimeZone(),scheduleDao,userDao,eTLAdminDao,commonJdbcTemplate);
			 message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Druid Integration executed successfully.");
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while alertsAndThreshoulds() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}  
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/reloadUrlIntegration", method = RequestMethod.POST)
	public DataResponse reloadUrlIntegration(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String schemaName = clientDbDetails.get("clientdb_schema").toString();
			MiniDwJobUtil.reloadUrl(userId, packageExecution.getDerivedTablesList(), clientIdfromHeader,schemaName,clientAppDbJdbcTemplate,authenticationEndPointUrl);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("reload Url Integration executed successfully.");
		} catch (Throwable ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while reloadUrlIntegration() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}  
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	 public static void druidIntegration(String clientIdfromHeader, String userId, List<String> totalTablesList,JdbcTemplate clientAppDbJdbcTemplate,
			 long executionId,String timeZone,ScheduleDao scheduleDao,UserDao userDao,ETLAdminDao eTLAdminDao,JdbcTemplate commonJdbcTemplate) { 
			
			Modification modification = null;
			MiddleLevelManager middleLevelManager = null;

				try {
					
					CloudClient cloudClient = userDao.getClientDetails(clientIdfromHeader);
				    timeZone = StringUtils.isBlank(timeZone)?TimeZone.getDefault().getID():timeZone ;
					
					modification = new Modification(new Date());
					modification.setCreatedBy(userId);
					modification.setModifiedBy(userId);
					
					if (cloudClient.isDruidEnabled()) {
						
					middleLevelManager = eTLAdminDao.getMiddleLevelManagerDetailsById(commonJdbcTemplate);				
					DruidIntegration druidIntegration = new DruidIntegration(totalTablesList, userId,middleLevelManager,clientIdfromHeader,scheduleDao,modification,executionId,timeZone,clientAppDbJdbcTemplate);
					druidIntegration.start();
					
					}else {
						PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId,Constants.ExecutionStatus.IGNORED, "Druid is not not enabled this client id:"+clientIdfromHeader, timeZone);
						packExecutionAfter.setModification(modification);
						scheduleDao.updateDruidStartInfo(packExecutionAfter, clientAppDbJdbcTemplate);
					} 
				} catch (Throwable t) {
					PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId,Constants.ExecutionStatus.FAILED,"\n"+t.getMessage(), timeZone);
					packExecutionAfter.setModification(modification);
					scheduleDao.updateDruidEndInfo(packExecutionAfter, clientAppDbJdbcTemplate);
				}

		     }
	 
	   public CustomRequest getCustomRequest(String deploymentType, String userId, String clientId) {
			CustomRequest customRequest = new CustomRequest(null, clientId, clientId, null, deploymentType, null, userId);
			return customRequest;
		}

	@RequestMapping(value = "/updateRetryPaginationToNull", method = RequestMethod.POST)
	public DataResponse updateRetryPaginationToNull(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody DLInfo dlInfo, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			ILInfo[] ilList = dlInfo.getIlList();
			List<Integer> mappingList = new ArrayList<Integer>();
			for (int i = 0; i < ilList.length; i++)
			{
				List<ILConnectionMapping>  iLConnectionMappingList = ilList[i].getIlSources();
				
				for(ILConnectionMapping iLConnectionMapping : iLConnectionMappingList)
				{
					mappingList.add(iLConnectionMapping.getConnectionMappingId());
				}
			}
			if( mappingList != null && mappingList.size() > 0 )
			{
				 scheduleService.updateRetryPaginationToNull(mappingList, clientAppDbJdbcTemplate);
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Retry Pagination To Null  successfully updated.");
		}
		catch ( Throwable ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while updateRetryPaginationToNull() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
}
