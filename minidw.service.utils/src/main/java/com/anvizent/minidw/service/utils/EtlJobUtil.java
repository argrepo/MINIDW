package com.anvizent.minidw.service.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.anvizent.minidw.service.utils.helper.StreamGobbler;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.datamodel.anvizent.helper.Constants;

/**
 * Hello world!
 *
 */
public class EtlJobUtil {

	protected static final Log LOG = LogFactory.getLog(EtlJobUtil.class);
	
	private String commonEtlJobsFolder;
	private String etlJobsFolder;
	private String systemPathSeperator;

	public EtlJobUtil(String commonEtlJobsFolder, String etlJobsFolder, String systemPathSeperator) {
		this.commonEtlJobsFolder = commonEtlJobsFolder.replaceAll("\\\\", "/");
		this.etlJobsFolder = etlJobsFolder.replaceAll("\\\\", "/");
		this.systemPathSeperator = systemPathSeperator;
	}

	public String[] getETLExecCommand(String dependentJars, String jobMainClass, String[] contextparmsArray) {
		List<String> list = new ArrayList<>();
		list.add("java");
		if (StringUtils.isNotBlank(Constants.Config.JAVA_OPT_PARAMS)) {
			list.addAll(Arrays.asList(StringUtils.split(Constants.Config.JAVA_OPT_PARAMS, " ")));
		}
		list.add("-cp");

		String commonlib = commonETLLib(commonEtlJobsFolder);
		StringBuilder sb = new StringBuilder();
		sb.append(commonlib);

		String ildependentjars = dependentJars;
		String[] il_jars_array = ildependentjars.split(",");

		for (int i = 0; i < il_jars_array.length; i++) {
			String il_jar = il_jars_array[i];
			sb.append(etlJobsFolder + "/" + il_jar);
			if (i < il_jars_array.length - 1) {
				sb.append(systemPathSeperator);
			}
		}

		String jars = sb.toString();
		list.add(jars);
		list.add(jobMainClass);

		for (int i = 0; i < contextparmsArray.length; i++) {
			list.add(contextparmsArray[i]);
		}

		String[] command = new String[list.size()];
		command = list.toArray(command);
		/*System.out.println("Etl Jobs command --> ");
		for ( String commondLine : list ) {
			System.out.println(commondLine);
		}*/
		return command;
	}

	public String commonETLLib(String dir) {
		File[] files = CommonUtils.getFiles(dir, Constants.FileType.JAR);
		StringBuilder sb = new StringBuilder();

		for (File file : files) {
			sb.append(file.getAbsolutePath());
			sb.append(systemPathSeperator);
		}

		return sb.toString();
	}

	public ETLjobExecutionMessages runETLjar(String jobFileName, String dependencyJARs, String[] ilContextParamsArr)
			throws InterruptedException, IOException {

		String[] commandforExecution = getETLExecCommand(dependencyJARs, jobFileName, ilContextParamsArr);
		Process proc;
		int exitStatus = 0;
		try {
			proc = Runtime.getRuntime().exec(commandforExecution);
		} catch (NullPointerException e) {
			throw new InterruptedException("Job name found null");
		}

		StreamGobbler errorStreamGobbler = new StreamGobbler(proc.getErrorStream());
		StreamGobbler inputStreamGobbler = new StreamGobbler(proc.getInputStream());
		inputStreamGobbler.start();
		errorStreamGobbler.start();
		
		exitStatus = proc.waitFor();
		
		String inputStreamMsg = inputStreamGobbler.getOutput();
		String errorStreamMsg = errorStreamGobbler.getOutput();

		if (StringUtils.isNotBlank(errorStreamMsg) && errorStreamMsg.contains("Could not find or load main class local_project")) {
			throw new InterruptedException(errorStreamMsg);
		}
		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		etlJobExecutionMessages.setInputStreamMsg(inputStreamMsg);
		etlJobExecutionMessages.setErrorStreamMsg(errorStreamMsg);
		etlJobExecutionMessages.setStatus(exitStatus);

		return etlJobExecutionMessages;
	}
	
	public ETLjobExecutionMessages runETLjar(String jobFileName, String dependencyJARs, Map<String, Object> ilContextParamsArr)
			throws InterruptedException, IOException {

		String[] commandforExecution = getETLExecCommand(dependencyJARs, jobFileName, convertToContextParamsArray(ilContextParamsArr));
		Process proc;
		int exitStatus = 0;
		try {
			proc = Runtime.getRuntime().exec(commandforExecution);
		} catch (NullPointerException e) {
			throw new InterruptedException("Job name found null");
		}

		StreamGobbler errorStreamGobbler = new StreamGobbler(proc.getErrorStream());
		StreamGobbler inputStreamGobbler = new StreamGobbler(proc.getInputStream());
		inputStreamGobbler.start();
		errorStreamGobbler.start();
		
		exitStatus = proc.waitFor();
		
		String inputStreamMsg = inputStreamGobbler.getOutput();
		String errorStreamMsg = errorStreamGobbler.getOutput();


		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		etlJobExecutionMessages.setInputStreamMsg(inputStreamMsg);
		etlJobExecutionMessages.setErrorStreamMsg(errorStreamMsg);
		etlJobExecutionMessages.setStatus(exitStatus);

		return etlJobExecutionMessages;
	}
	
	public static String[] convertToContextParamsArray(Map<String, Object> params) {
		if (params == null || params.size() == 0)
			return new String[0];

		String[] parameters = new String[params.size()];

		Set<Map.Entry<String, Object>> set = params.entrySet();

		int index = 0;
		for (Map.Entry<String, Object> entry : set) {
			String param = "--context_param " + entry.getKey() + "=" + entry.getValue();
			parameters[index] = param;
			index++;
		}

		return parameters;
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		runjar();
	}
	
	public static ETLjobExecutionMessages runjar()
			throws InterruptedException, IOException {

		String[] commandforExecution = new String[]{"java" ,"-Xms256M", "-Xmx1024M","-jar","C:/Users/rajesh.anthari/Desktop/samplejob-0.1.jar"};
		Process proc;
		int exitStatus = 0;
		try {
			LOG.debug("Execution started");
			proc = Runtime.getRuntime().exec(commandforExecution);
			LOG.debug("Execution started");
		} catch (NullPointerException e) {
			throw new InterruptedException("Job name found null");
		}

		StreamGobbler errorStreamGobbler = new StreamGobbler(proc.getErrorStream());
		StreamGobbler inputStreamGobbler = new StreamGobbler(proc.getInputStream());
		inputStreamGobbler.start();
		errorStreamGobbler.start();
		
		exitStatus = proc.waitFor();
		
		String inputStreamMsg = inputStreamGobbler.getOutput();
		String errorStreamMsg = errorStreamGobbler.getOutput();

		if (StringUtils.isNotBlank(errorStreamMsg) && errorStreamMsg.contains("Could not find or load main class local_project")) {
			throw new InterruptedException(errorStreamMsg);
		}
		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		etlJobExecutionMessages.setInputStreamMsg(inputStreamMsg);
		etlJobExecutionMessages.setErrorStreamMsg(errorStreamMsg);
		etlJobExecutionMessages.setStatus(exitStatus);

		return etlJobExecutionMessages;
	}
	
	
}
