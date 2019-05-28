package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ELTCommandConstants;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;

@Component
public class CommonProcessor {
	
	protected static final Log log = LogFactory.getLog(CommonProcessor.class);
	
	Set<String> exemptionParamsList = new HashSet<String>();
	
	public CommonProcessor() {
		exemptionParamsList.add("SRC_PW");
		exemptionParamsList.add("TGT_PW");
		exemptionParamsList.add("MASTER_PW");
	}
	
	
	public PackageExecution getUploadStatus(long executionId, String status, String comments, String timeZone){
			PackageExecution packageExecution = new PackageExecution();
			packageExecution.setExecutionId(executionId);
			packageExecution.setUploadStatus(status);
			packageExecution.setUploadComments(comments);
			packageExecution.setUploadStartDate(getFormattedDateString());
			packageExecution.setLastUploadedDate(getFormattedDateString());
			Modification modification = new Modification(new Date());
			modification.setCreatedBy("");
			packageExecution.setModification(modification);
		return packageExecution;
	  }
	
	public PackageExecution getExecutionStatus(long executionId, String status, String comments,
			String timeZone) {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution.setExecutionId(executionId);
		packageExecution.setExecutionStatus(status);
		packageExecution.setExecutionComments(comments);
		packageExecution.setExecutionStartDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setLastExecutedDate(TimeZoneDateHelper.getFormattedDateString());
		Modification modification = new Modification(new Date());
		modification.setCreatedBy("");
		packageExecution.setModification(modification);
		return packageExecution;
	}
	
	public PackageExecution packageExecutionMappingInfo(String status, String comments, long mappingId,
			Modification modification, String timeZone) throws ParseException {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution = new PackageExecution();
		packageExecution.setExecutionStatus(status);
		packageExecution.setExecutionComments(comments);
		packageExecution.setExecutionStartDate(getFormattedDateString());
		packageExecution.setLastExecutedDate(getFormattedDateString());
		packageExecution.setMappingId(mappingId);
		packageExecution.setModification(modification);
		return packageExecution;
	}
	
	public String getFormattedDateString() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		return sdf.format(date).toString();
	}

	public String generateUniqueIdWithTimestamp() {
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
	
	public String currentDateTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String cTime = currentTime.format(formatter);
		return cTime;
	}
	
	public int runDDlayoutTable(DDLayout ddlayout, JdbcTemplate clientJdbcTemplate) throws SQLException {		
		int insertCount = -1;
		int selectQryCountFromTemp = -1;
		String randomUUID = UUID.randomUUID().toString();
		String tempTableName = "temp_"+StringUtils.replace(randomUUID, "-", "_");
		
		String tempTableCreateStatement = "create table "+ tempTableName +" as ("+ ddlayout.getSelectQry() +")";
		String tempTableDropStatement = "drop table if exists "+ tempTableName;
		String tempSelectQuery = "select count(*) from "+tempTableName;
		 try {
			 clientJdbcTemplate.execute(tempTableCreateStatement);
			 selectQryCountFromTemp = clientJdbcTemplate.queryForObject(tempSelectQuery, Integer.class);
			 
			 if(selectQryCountFromTemp > 0){
				clientJdbcTemplate.execute("truncate table " + ddlayout.getTableName() + ";");
				String insertQry = "INSERT INTO " + ddlayout.getTableName() + " ( select * from " + tempTableName + " ) ";
				insertCount = clientJdbcTemplate.update(insertQry);
				if(insertCount == 0 ){
					throw new SQLException(" Insertion failed into DDL table "+ ddlayout.getTableName());
				}
			  }else{
				    throw new SQLException(selectQryCountFromTemp + " records found in source query");
			  }
		 }catch(Exception e) {
			 throw new SQLException(e);
		 }finally {
			 clientJdbcTemplate.execute(tempTableDropStatement);
		 }
		return insertCount;
	}
	
	public String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				System.out.println("dir created:" + dirName);
			}
		}
		return dirName;
	}
	
	public String getScheduleStartTime(Schedule schedule) {
		String scheduleStartTime = null;
		scheduleStartTime = (schedule.getScheduleStartDate() != null ? schedule.getScheduleStartDate(): "-") + " " + ( schedule.getScheduleStartTime() != null ? schedule.getScheduleStartTime():"-");

		return scheduleStartTime;

	}
	
	public String getscheduleRecurrence(Schedule schedule) {
		String scheduleRecurrence = null;
		if (schedule.getRecurrencePattern().equals("runnow")) {
			scheduleRecurrence = "Run Now";
		}
		if (schedule.getRecurrencePattern().equals("minutes")) {
			if (schedule.getMinutesToRun() != 0)
			scheduleRecurrence = "on every " +schedule.getMinutesToRun()+ " minutes";
		}
		if (schedule.getRecurrencePattern().equals("hourly")) {
			String typeOfHour = schedule.getTypeOfHours();
			if (StringUtils.isNotBlank(typeOfHour)) {
				if (typeOfHour.equals("every")) {
					scheduleRecurrence = "for every " + schedule.getHoursToRun() + " hours";
				}
				if (typeOfHour.equals("selected")) {
					scheduleRecurrence = "on " + schedule.getHoursToRun() + " hours";
				}
				
			} else {
				scheduleRecurrence = schedule.getRecurrencePattern();
			}
			
			
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

	}
	
	public String getScheduleRange(Schedule schedule) {
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

	}
	
	public void moveErrorLogFile(String clientIdOrUserId, String packageId,String jobName, String jobStartDate, String errorLogTableName,String filePath) throws IOException {
		String dateVal = jobStartDate.replaceAll(":", "-");
		String batchId = clientIdOrUserId + "_" + packageId + "_"
				+ jobName + "_" + dateVal + ".csv";
		File file = new File(
				filePath + errorLogTableName + File.separator + batchId);
		String destinationPath = Constants.Config.ETL_JOBS_LOGS;
		
		if (file.exists()) {
			FileUtils.copyFileToDirectory(file, new File(destinationPath));
		}
	}
	

	public Map<String, String> getPrintableParams(Map<String, String> contextParams) {
		Map<String, String> clonedParams = new HashMap<String, String>();
		clonedParams.putAll(contextParams);
		clonedParams.keySet().removeAll(exemptionParamsList);
		return clonedParams;
	}
	
	public void parseContextParams(final Map<String, String> contextParams,
			final Map<String, String> paramsVals) {

		Set<Map.Entry<String, String>> set = contextParams.entrySet();

		for (Map.Entry<String, String> entry : set) {
			String paramval = entry.getValue();
			if (paramval.indexOf("{") != -1) {
				int endindex = paramval.indexOf("}");
				String key = paramval.substring(1, endindex);
				String value = paramsVals.get(key);

				if (value != null) {
					entry.setValue(paramval.replace("{" + key + "}", value));
				} else
					System.out.println(key + " --> " + paramval + " --> " + value);
			}
		}
	}
	
	
	public JSONObject getExternalDataJSON(long package_id, long schedule_id, long execution_id, String client_id, int dlid, int ilid) {
		
		JSONObject externalDataJSON = new JSONObject();
		externalDataJSON.put(ELTCommandConstants.PACKAGEID, package_id);
		externalDataJSON.put(ELTCommandConstants.SCHEDULEID, schedule_id);
		externalDataJSON.put(ELTCommandConstants.EXECUTIONID, execution_id);
		externalDataJSON.put(ELTCommandConstants.CLIENTID, client_id);
		externalDataJSON.put(ELTCommandConstants.DLID, dlid);
		externalDataJSON.put(ELTCommandConstants.ilID,ilid);
		
		return externalDataJSON;
	}

}
