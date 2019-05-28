package com.datamodel.anvizent.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anvizent.minidw.service.utils.helper.StreamGobbler;

class AlertsAndThreshoulds extends Thread {
	protected static final Log LOGGER = LogFactory.getLog(AlertsAndThreshoulds.class);
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
					new String[] { "java", "-jar",com.datamodel.anvizent.helper.CommonUtils.getEtlJobsPath() + "/threshold-check-initiator.jar",
							alertsThresholdsUrl, clientId, commaSeparatedTableNames });
		} catch (Throwable t) {
			LOGGER.info("Alerts and threshoulds execution failed.", t);
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
		LOGGER.info(inputStreamGobbler.getOutput() + "\n" + errorDetails);
		System.out.println(errorDetails);
		if (0 != exitStatus) {
			throw new Exception(errorDetails);
		} else {
			return inputStreamGobbler.getOutput() + "\n" + errorDetails;
		}
	}
}