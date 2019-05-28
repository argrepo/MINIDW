package com.anvizent.minidw.service.utils;

import java.io.IOException;
import java.util.StringJoiner;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.anvizent.minidw.service.utils.helper.StreamGobbler;
import com.anvizent.minidw.service.utils.model.RJobExecutionMessages;

/**
 * @author mahender.alaveni
 * 
 * RJobUtil is used to run the R file using rscript.exe
 *
 */
public class RJobUtil {

	protected static final Log LOG = LogFactory.getLog(RJobUtil.class);

	String rscriptPath;
	String rJobpath;
	public RJobUtil(String rscriptPath,String rJobpath )
	{
		this.rscriptPath = rscriptPath;
		this.rJobpath = rJobpath;
	}
	
	public RJobExecutionMessages runRJob(String  rJobContextParamsPath)
			throws InterruptedException, IOException {

		String commandforExecution = getRExecCommand(rscriptPath,rJobpath,rJobContextParamsPath);
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
		RJobExecutionMessages rJobExecutionMessages = new RJobExecutionMessages();
		rJobExecutionMessages.setInputStreamMsg(inputStreamMsg);
		rJobExecutionMessages.setErrorStreamMsg(errorStreamMsg);
		rJobExecutionMessages.setStatus(exitStatus);

		return rJobExecutionMessages;
	}
	
	private String getRExecCommand(String rScriptPath, String rJobPath,String rJobContextParamsPath)
	{
		StringJoiner stringJoiner = new StringJoiner(" ");
		stringJoiner.add(rScriptPath);
		stringJoiner.add(rJobPath);
		stringJoiner.add(rJobContextParamsPath);
		return stringJoiner.toString();
	}
	
}
