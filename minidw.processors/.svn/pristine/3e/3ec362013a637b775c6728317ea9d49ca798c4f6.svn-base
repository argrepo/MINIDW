package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Package;

@Component
public class PreparedObjectProcessor {
	
	@Autowired
	CommonProcessor commonProcessor;
	
	public Map<String, String> getContextParams(Map<String, String> ilContextParams, String stagingDbHost,
			String stagingDbPort, String stagingClientSchema, String stagingDbUname, String stagingDbPwd,
			String tagetDbHost, String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd,
			String masterDbHost, String masterDbPort, String masterClientSchema, String masterDbUname,
			String masterDbPwd, String dbTypeName, ILInfo iLInfo, String clientId, int packageId, String jobFilesPath,
			long executionId,ClientJobExecutionParameters clientJobExecutionParameters,boolean loadType, int dlId) {

		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		String datasourcename = null;
		
		jobFilesPath =  jobFilesPath + clientId + "_" + dlId + "_" + iLInfo.getiL_id()+"/";
		
		createDir(jobFilesPath);
		
		// staging properties
		ilParamsVals.put("src_host", stagingDbHost);
		ilParamsVals.put("src_port", stagingDbPort);
		ilParamsVals.put("src_database", stagingClientSchema);
		ilParamsVals.put("src_user", stagingDbUname);
		ilParamsVals.put("src_pass", stagingDbPwd);

		// target database properties
		ilParamsVals.put("tgt_host", tagetDbHost);
		ilParamsVals.put("tgt_port", tagetDbPort);
		ilParamsVals.put("tgt_database", tagetClientSchema);
		ilParamsVals.put("tgt_user", tagetDbUname);
		ilParamsVals.put("tgt_pass", tagetDbPwd);

		// master database properties
		ilParamsVals.put("master_host", masterDbHost);
		ilParamsVals.put("master_port", masterDbPort);
		ilParamsVals.put("master_database", masterClientSchema);
		ilParamsVals.put("master_user", masterDbUname);
		ilParamsVals.put("master_pass", masterDbPwd);

		// staging table and target table
		ilParamsVals.put("tgt_table", iLInfo.getiL_table_name());
		ilParamsVals.put("stg_table", iLInfo.getiL_table_name() + "_Stg");

		// client id and package id
		ilParamsVals.put("client_id", clientId);
		ilParamsVals.put("package_id", packageId + "_" + dlId + "_" + executionId);

		ilParamsVals.put("error_log_path", jobFilesPath);
		ilParamsVals.put("bulk_path",jobFilesPath);

		ilParamsVals.put("job_name", iLInfo.getiL_table_name());
		String jobType = "IL";
		ilParamsVals.put("job_type", jobType);

		datasourcename = StringUtils.isNotBlank(dbTypeName) ? dbTypeName : "unknown";
		ilParamsVals.put("datasourcename", datasourcename);
		//client jobs execution params
		ilParamsVals.put("src_time_zone", convertToGSTFormat(clientJobExecutionParameters.getSourceTimeZone()));
		ilParamsVals.put("tgt_time_zone", convertToGSTFormat(clientJobExecutionParameters.getDestTimeZone()));
		ilParamsVals.put("case_sensitive_data_set", clientJobExecutionParameters.getCaseSensitive()+"");
		ilParamsVals.put("null_replacement_string", clientJobExecutionParameters.getNullReplaceValues());
		ilParamsVals.put("load_type", loadType ?  "Incremental" : "Full");
		ilParamsVals.put("trailing_months", String.valueOf(clientJobExecutionParameters.getInterval()));
		
		System.out.println(ilContextParams);
		parseContextParams(ilContextParams, ilParamsVals);

		return ilContextParams;
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
	
	private String convertToGSTFormat(String timezoneFormat) {
		// as per the request we are passing full time zone to the job
		if ( timezoneFormat != null ) {
			return timezoneFormat;
		}
		String timezoneInGST= null;
		try{
			TimeZone tz = TimeZone.getTimeZone(timezoneFormat);
			Calendar cal = GregorianCalendar.getInstance(tz);
		    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
		    String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
		    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
		    timezoneInGST= "GMT"+offset;
		}catch(Exception e){
			e.printStackTrace();
		}
		return timezoneInGST;
	}
	
	public Map<String, String> getDlParamsVals(String stagingDbHost, String stagingDbPort,
			String stagingClientSchema, String stagingDbUname, String stagingDbPwd, String tagetDbHost,
			String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd, String masterDbHost,
			String masterDbPort, String masterClientSchema, String masterDbUname, String masterDbPwd, String clientId,
			Package userPackage, String jobFilesPath, long executionId,ClientJobExecutionParameters clientJobExecutionParameters,
			boolean loadType, int dlId) {

		Map<String, String> dlParamsVals = new LinkedHashMap<>();
		
		jobFilesPath = jobFilesPath + clientId + "_" + dlId+"/";

		createDir(jobFilesPath);
		
		// staging database
		dlParamsVals.put("src_host", stagingDbHost);
		dlParamsVals.put("src_port", stagingDbPort);
		dlParamsVals.put("src_database", stagingClientSchema);
		dlParamsVals.put("src_user", stagingDbUname);
		dlParamsVals.put("src_pass", stagingDbPwd);

		// target database
		dlParamsVals.put("tgt_host", tagetDbHost);
		dlParamsVals.put("tgt_port", tagetDbPort);
		dlParamsVals.put("tgt_database", tagetClientSchema);
		dlParamsVals.put("tgt_user", tagetDbUname);
		dlParamsVals.put("tgt_pass", tagetDbPwd);

		// master database

		dlParamsVals.put("master_host", masterDbHost);
		dlParamsVals.put("master_port", masterDbPort);
		dlParamsVals.put("master_database", masterClientSchema);
		dlParamsVals.put("master_user", masterDbUname);
		dlParamsVals.put("master_pass", masterDbPwd);

		// file path details
		dlParamsVals.put("error_log_path",jobFilesPath);
		dlParamsVals.put("bulk_path",jobFilesPath);

		dlParamsVals.put("start_date_time", commonProcessor.currentDateTime());

		// client id and package id
		dlParamsVals.put("client_id", clientId);
		dlParamsVals.put("package_id", userPackage.getPackageId() + "_" + dlId + "_" + executionId);
		dlParamsVals.put("datasourcename", "N");
		
		//client jobs execution params
		dlParamsVals.put("src_time_zone", convertToGSTFormat(clientJobExecutionParameters.getSourceTimeZone()));
		dlParamsVals.put("tgt_time_zone", convertToGSTFormat(clientJobExecutionParameters.getDestTimeZone()));
		dlParamsVals.put("case_sensitive_data_set", clientJobExecutionParameters.getCaseSensitive()+"");
		dlParamsVals.put("null_replacement_string", clientJobExecutionParameters.getNullReplaceValues());
		dlParamsVals.put("load_type", loadType ?  "Incremental" : "Full");
		dlParamsVals.put("trailing_months", String.valueOf(clientJobExecutionParameters.getInterval()));

		return dlParamsVals;
	}

}
