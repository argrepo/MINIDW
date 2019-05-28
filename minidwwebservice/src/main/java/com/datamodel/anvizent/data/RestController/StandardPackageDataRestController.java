package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.ScheduleService;
import com.datamodel.anvizent.service.StandardPackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.WSService;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataTypesInfo;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;

@Import(AppProperties.class)
@RestController("user_standardPackageDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class StandardPackageDataRestController
{

	protected static final Log LOGGER = LogFactory.getLog(StandardPackageDataRestController.class);
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;

	@Autowired
	private StandardPackageService standardPackageService;

	@Autowired
	private PackageService packageService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	ExecutionProcessor executionProcessor;

	@Autowired
	private WSService wSService;

	@Autowired
	CommonProcessor commonProcessor;

	/**
	 * @param clientId
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getStandardPackage", method = RequestMethod.GET)
	public DataResponse getStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Package spInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			spInfo = standardPackageService.fetchStandardPackageInfo(clientId, clientAppDbJdbcTemplate);
			if( spInfo != null )
			{
				dataResponse.setObject(spInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}
			else
			{

				// create new Default Standard Package
				Package createUserPackage = new Package();
				createUserPackage.setPackageId(0);
				createUserPackage.setPackageName("Standard Package");
				createUserPackage.setIsStandard(Boolean.TRUE);
				createUserPackage.setIsScheduled(Boolean.FALSE);
				createUserPackage.setIsMapped(Boolean.FALSE);
				createUserPackage.setScheduleStatus(Constants.Status.STATUS_PENDING);
				createUserPackage.setTrailingMonths(6);
				createUserPackage.setIsAdvanced(Boolean.FALSE);
				createUserPackage.setUserId(clientId);

				Industry industry = new Industry();
				industry.setId(0);
				createUserPackage.setIndustry(industry);
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				createUserPackage.setModification(modification);

				int packageId = standardPackageService.createStandardPackage(createUserPackage, clientAppDbJdbcTemplate);
				if( packageId == 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.packageCreatedSuccesfully", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					dataResponse.setObject(createUserPackage);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetPackageData", null, locale));
					messages.add(message);
				}
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetPackageData", null, locale));
			messages.add(message);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/getClientStandardPackageDLs", method = RequestMethod.GET)
	public DataResponse getClientsDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{

		List<DLInfo> dlList = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dlList = standardPackageService.getClientSPDLs(clientIdfromHeader, clientAppDbJdbcTemplate);
			if( dlList != null && dlList.size() > 0 )
			{
				for (DLInfo dlinfo : dlList)
				{
					if( dlinfo.getSchedule().getScheduleId() != 0 )
					{
						Schedule schedule = new Schedule();
						schedule.setScheduleId(dlinfo.getSchedule().getScheduleId());
						schedule.setRecurrencePattern(dlinfo.getSchedule().getRecurrencePattern());
						schedule.setScheduleStartTime(commonProcessor.getScheduleStartTime(dlinfo.getSchedule()));
						schedule.setScheduleRecurrence(commonProcessor.getscheduleRecurrence(dlinfo.getSchedule()));
						schedule.setScheduleRange(commonProcessor.getScheduleRange(dlinfo.getSchedule()));
						schedule.setScheduleType(dlinfo.getSchedule().getScheduleType());
						schedule.setTimeZone(dlinfo.getSchedule().getTimeZone());
						dlinfo.setSchedule(schedule);
					}
				}
				dataResponse.setObject(dlList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDLClientDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param dlId
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getSourceInfo/{dlId}", method = RequestMethod.GET)
	public DataResponse getDlSourceInfoForStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("dlId") Integer dlId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientId = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			DLInfo dlInfo = standardPackageService.getIlMappingInfobyId(userId, clientId, dlId, clientAppDbJdbcTemplate);
			if( dlInfo != null )
			{
				DLInfo dlById = packageService.getDLById(dlId, clientId, clientAppDbJdbcTemplate);
				dlInfo.setdL_id(dlById.getdL_id());
				dlInfo.setdL_name(dlById.getdL_name());
				dlInfo.setdL_table_name(dlById.getdL_table_name());
				dlInfo.setDescription(dlById.getDescription());

				List<String> totalTablesSet = new ArrayList<String>(Arrays.asList(dlInfo.getdL_table_name()));
				List<DDLayout> dDLayoutList = packageService.getDDlayoutList(clientId, totalTablesSet, userId, clientAppDbJdbcTemplate);
				dlInfo.setDdlayoutList(dDLayoutList);

				for (ILInfo mappingInfo : dlInfo.getIlList())
				{
					mappingInfo.setIlSources(MinidwServiceUtil.getILConnectionMapping(mappingInfo.getIlSources()));
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(dlInfo);
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
			messages.add(message);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param dlId
	 * @param iLid
	 * @param reques
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getSourceInfo/{dlId}/{iLId}", method = RequestMethod.GET)
	public DataResponse getIlSourceInfoForStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("dlId") Integer dlId, @PathVariable("iLId") Integer iLid, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientId = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			DLInfo dlInfo = standardPackageService.getIlMappingInfobyId(userId, clientId, dlId, iLid, clientAppDbJdbcTemplate);
			if( dlInfo != null )
			{
				dlInfo.getIlInfo().setIlSources(MinidwServiceUtil.getILConnectionMapping(dlInfo.getIlInfo().getIlSources()));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(dlInfo);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.nomappinginfofound", null, locale));
				messages.add(message);
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
			messages.add(message);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param request
	 * @param locale
	 * @param dlInfo
	 * @return
	 */
	@RequestMapping(value = "/getILConnectionMappingInfoByMappingIds", method = RequestMethod.POST)
	public DataResponse runDLSources(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale, @RequestBody DLInfo dlInfo)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<Integer> mappingsIds = new ArrayList<>();
		try
		{

			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			for (ILInfo il : dlInfo.getIlList())
			{
				il.getIlSources().forEach(mappingInfo ->
				{
					mappingsIds.add(mappingInfo.getConnectionMappingId());
				});
			}
			List<ILConnectionMapping> ilConnectionMappings = standardPackageService.getILConnectionMappingInfoByMappingId(mappingsIds, userId, clientAppDbJdbcTemplate);
			ilConnectionMappings = MinidwServiceUtil.getILConnectionMapping(ilConnectionMappings);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.addMessage(message);
			dataResponse.setObject(ilConnectionMappings);
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetIlConnectionMappingInfo", null, locale) + t.getMessage());
			dataResponse.addMessage(message);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/runDDlayoutsById", method = RequestMethod.POST)
	public DataResponse runDDlayoutList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody Set<Integer> ddLTableSet, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setModifiedBy(userId);
			modification.setCreatedBy(userId);

			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(userId);
			String runType = com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUNNOW;

			return executionProcessor.runCustomDataSetsInStandardPackage(ddLTableSet, user, clientJdbcTemplate, clientAppDbJdbcTemplate, modification, runType);
		}
		catch ( Throwable ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while runDDlayoutList() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveDLSchedule", method = RequestMethod.POST)
	public DataResponse saveScheduleInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody ClientData clientData, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Integer scheduleId = clientData.getSchedule().getScheduleId();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			clientData.setUserId(userId);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			Schedule scheduleInfo = clientData.getSchedule();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			modification.setIsActive(true);
			scheduleInfo.setModification(modification);
			clientData.setModification(modification);

			if( clientData.getUserPackage() != null )
			{
				/* get cron expression using schedule */
				String cronExpression = generateCronExpression(scheduleInfo);
				if( StringUtils.isBlank(cronExpression) )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToScheduleWithGivenInputs", null, locale));
					return dataResponse;
				}
				if( clientData.getSchedule().getRecurrencePattern().equals("once") )
				{
					clientData.getSchedule().setTimeZone(TimeZone.getDefault().getID());
					clientData.getSchedule().setScheduleStartDate(null);
					clientData.getSchedule().setScheduleStartTime(null);
					clientData.getSchedule().setScheduleEndDate(null);
				}
				/* save or update schedule */
				if( clientData.getSchedule().getScheduleId() != null && clientData.getSchedule().getScheduleId() != 0 )
				{
					scheduleId = clientData.getSchedule().getScheduleId();
					clientData.getUserPackage().setScheduleType("Reschedule");
					scheduleService.updateSchedule(clientData, clientAppDbJdbcTemplate);

					/*
					 * update retry_pagination to null at
					 * minidwcs_package_web_service_source_mapping
					 */
					int dlId = clientData.getDlInfo().getdL_id();
					if( dlId != 0 )
					{
						List<Integer> mappingList = scheduleService.getPackageSourceMappingListByDlId(dlId, clientAppDbJdbcTemplate);
						if( mappingList != null && mappingList.size() > 0 ) scheduleService.updateRetryPaginationToNull(mappingList, clientAppDbJdbcTemplate);
					}
				}
				else
				{
					clientData.getUserPackage().setScheduleType("Schedule");
					scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
					clientData.getSchedule().setScheduleId((int) scheduleId);
				}
				clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
				clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
				clientData.getSchedule().setScheduleId((int) scheduleId);
				clientData.setModification(modification);
				/*
				 * packageService.updatePackageScheduleStatus(clientData,
				 * clientAppDbJdbcTemplate);
				 */
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.DLScheduleCreatedSuccessfully", new Object[] { clientData.getDlInfo().getdL_name() }, locale));
			}
		}
		catch ( Throwable e )
		{
			LOGGER.error("", e);
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		return dataResponse;
	}

	public String generateCronExpression(Schedule schedule) throws Exception
	{

		String dateTime = schedule.getScheduleStartTime();
		String cronTemplate = "0 {minutes} {hours} {dayofthemonth} {month} {dayoftheweek}";

		if( schedule.getScheduleStartDate() != null && schedule.getRecurrencePattern() != null )
		{
			LOGGER.info("RecurrencePattern selected" + schedule.getRecurrencePattern());
			if( schedule.getRecurrencePattern().equals("minutes") )
			{
				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", "0/" + schedule.getMinutesToRun());
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
			else if( schedule.getRecurrencePattern().equals("hourly") )
			{
				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				// 0 13 0/1 {dayofthemonth} {month} {dayoftheweek}
				if( StringUtils.isNotBlank(schedule.getTypeOfHours()) )
				{
					if( schedule.getTypeOfHours().equals("every") )
					{
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/" + schedule.getHoursToRun());
					}
					else
					{
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", schedule.getHoursToRun());
					}
				}
				else
				{
					cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/1");
				}
				// 0 13 0/1 * {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "*");
				// 0 13 0/1 * * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 0/1 * * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);
			}
			else if( schedule.getRecurrencePattern().equals("daily") )
			{
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
			else if( schedule.getRecurrencePattern().equals("weekly") )
			{
				// 0 09 15 ? * MONDAY,FRIDAY
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "?");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", schedule.getDaysToRun());

				schedule.setCronExpression(cronTemplate);
			}
			else if( schedule.getRecurrencePattern().equals("monthly") )
			{

				// in case if start date and day of month does not match
				int dayofmonth = Integer.parseInt(schedule.getDayOfMonth());
				// String daysTorun = schedule.getDaysToRun();
				String monthsToRun = schedule.getMonthsToRun();

				// int startMonth = localScheduleStartDate.getMonthValue();
				// 0 30 15 17 1/1 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", dayofmonth + "");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "1/" + monthsToRun);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);
			}
			else if( schedule.getRecurrencePattern().equals("yearly") )
			{
				// 0 30 15 17 2 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", schedule.getDayOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", schedule.getMonthOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);
			}
		}
		else
		{
			throw new Exception("Invalid Start date/Recurrence Pattern");
		}
		return cronTemplate;
	}

	@RequestMapping(value = "/unScheduleDL", method = RequestMethod.POST)
	public DataResponse unSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody Schedule schedule, Locale locale)
	{

		JdbcTemplate clientAppDbJdbcTemplate = null;

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		int status = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			schedule.setUserId(clientId);
			status = standardPackageService.updatePackageSchedule(schedule, clientAppDbJdbcTemplate);
			if( status == 1 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( AnvizentRuntimeException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( AnvizentCorewsException e )
		{
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getStandardPackageExecution", method = RequestMethod.POST)
	public DataResponse getPackageExecution(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, @RequestParam(value = "dlId") Integer dlId, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecution = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageExecution = standardPackageService.getPackageExecutionResults(dlId, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getStandardPackageExecutionByPagination", method = RequestMethod.POST)
	public DataResponse getStandardPackageExecutionByPagination(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, @RequestParam(value = "dlId") Integer dlId, @RequestParam(value = "offset") Integer offset, @RequestParam(value = "limit") Integer limit,
			Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecution = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageExecution = standardPackageService.getPackageExecutionResultsByPagination(dlId, offset, limit, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getStandardPackageExecutionCount", method = RequestMethod.POST)
	public DataResponse getStandardPackageExecutionCount(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, @RequestParam(value = "dlId") Integer dlId, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int count = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			count = standardPackageService.getStandardPackageExecutionCount(0, dlId, clientAppDbJdbcTemplate);
			dataResponse.setObject(count);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getStandardPackageJobResults/{packageId}", method = RequestMethod.GET)
	public DataResponse getStandardPackageJobResults(@PathVariable("packageId") String packageId, @PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("dlId") Integer dlId, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = standardPackageService.getExecutionJobResults(packageId, dlId, clientIdfromHeader, clientJdbcTemplate);
			dataResponse.setObject(resultlist);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
		}
		catch ( Exception e )
		{
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/saveDLTrailingMapping", method = RequestMethod.POST)
	public DataResponse saveDLTrailingMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("dL_id") Integer dL_id, @RequestParam("trailingMonths") Integer trailingMonths, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DLInfo dlInfo = new DLInfo();
		int create = 0;
		try
		{
			String clientId = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			dlInfo.setModification(modification);
			dlInfo.setdL_id(dL_id);
			dlInfo.setTrailingMonths(trailingMonths);
			create = standardPackageService.saveDLTrailingMapping(userId, clientId, dlInfo, clientAppDbJdbcTemplate);
			if( create > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
			messages.add(message);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateDLTrailing", method = RequestMethod.POST)
	public DataResponse updateDLTrailing(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("dL_id") Integer dL_id, @RequestParam("trailingMonths") Integer trailingMonths, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DLInfo dlInfo = new DLInfo();
		int create = 0;
		try
		{
			String clientId = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			dlInfo.setModification(modification);
			dlInfo.setdL_id(dL_id);
			dlInfo.setTrailingMonths(trailingMonths);
			create = standardPackageService.updateDLTrailingMapping(userId, clientId, dlInfo, clientAppDbJdbcTemplate);
			if( create > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
			messages.add(message);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getTimeZones", method = RequestMethod.GET)
	public DataResponse getTimeZones(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TimeZones> timeZoneList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			timeZoneList = wSService.getTimeZones();
			if( timeZoneList != null )
			{
				dataResponse.setObject(timeZoneList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while timeZoneList() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getAllWebServices", method = RequestMethod.GET)
	public DataResponse getAllWebServices(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<Integer, String> allWebservices = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			allWebservices = packageService.getAllWebServices(clientIdfromHeader, clientAppDbJdbcTemplate);
			if( allWebservices != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allWebservices);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.webserviceNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Exception e )
		{
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingWebserviceDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getStandardPackageJobResultsByDate/{packageId}/{dlId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("packageId") String packageId, @PathVariable("dlId") String dlId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, HttpServletRequest request,
			Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = standardPackageService.getExecutionJobResultsByDate(packageId, dlId, clientIdfromHeader, fromDate, toDate, clientJdbcTemplate);

		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Exception e )
		{
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDataTypesList", method = RequestMethod.GET)
	public DataResponse getDataTypesList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<DataTypesInfo> DataTypesList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			DataTypesList = packageService.getDataTypesList();
			if( DataTypesList != null )
			{
				dataResponse.setObject(DataTypesList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while DataTypesList() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPackageExecutionByPaginationId", method = RequestMethod.POST)
	public DataResponse getPackageExecutionByPaginationId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestParam("dlId") int dlId, @RequestParam(value = "offset") int offset, @RequestParam(value = "limit") int limit, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<PackageExecution> packageExecutionList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			if( dlId != 0 )
			{
				if( offset != 0 )
				{
					offset = offset * limit;
				}
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				packageExecutionList = standardPackageService.getPackageExecutionResultsByPagination(dlId, offset, limit, clientAppDbJdbcTemplate);

				String timeZone = CommonUtils.getTimeZoneFromHeader(request);

				for (PackageExecution packageExecution : packageExecutionList)
				{
					packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
					packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));

					packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
					packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
					packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
					packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
				}
				dataResponse.setObject(packageExecutionList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getPackageExecutionByPaginationId() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText("unable to retrieve pakage execution info.");
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
}
