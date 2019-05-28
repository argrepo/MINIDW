package com.datamodel.anvizent.helper.minidw;

public class MasterAndSlaveEndPoints {
	final public static class Master {
		public static final String MODULE_NAME = "/schedulermaster";
		public static final String START_SERVER = MODULE_NAME + "/masterserver/startScheduler";
		public static final String SERVER_STATUS = MODULE_NAME + "/masterserver/getSchedulerStatus";
		public static final String STOP_SERVER = MODULE_NAME + "/masterserver/stopScheduler";
		public static final String MASTER_SCHEDULER_PAUSE_JOB = MODULE_NAME + "/masterserver/pauseJobDetails";
		public static final String MASTER_SCHEDULER_RESUME_JOB = MODULE_NAME + "/masterserver/resumeJobDetails";
		public static final String MASTER_SCHEDULER_ID = MODULE_NAME + "/masterserver/getSchedulerId";
		public static final String MASTER_UPLOAD_EXECUTION_PACKAGE_PUSHER = MODULE_NAME
				+ "/masterserver/getUploadAndExecutionQueue";
		public static final String STORE_JOBS = MODULE_NAME + "/masterserver/storeJobs";
		public static final String RESTORE_JOBS = MODULE_NAME + "/masterserver/restoreJobs";
		public static final String PUSH_UPLOAD_QUEUE_TO_SLAVES = MODULE_NAME + "/masterserver/pushUploadQueueToSlaves";
		public static final String PUSH_EXECUTE_QUEUE_TO_SLAVES = MODULE_NAME + "/masterserver/pushExecuteQueueToSlaves";
		public static final String REFRESH_SLAVES_STATUS = MODULE_NAME + "/masterserver/refreshSlavesStatus";
		
		public static final String REMOVE_UPLOAD_Q_JOBS = MODULE_NAME + "/masterserver/removeUploadQJobs";
	
	}

	final public static class Slave {
		public static final String MODULE_NAME = "/minidwpackagerunnerapi";
		public static final String START_SLAVE = MODULE_NAME + "/startSlave";
		public static final String SLAVE_STATUS = MODULE_NAME + "/slaveStatus";
		public static final String STOP_SLAVE = MODULE_NAME + "/stopSlave";
		public static final String PACKAGE_EXECUTER = MODULE_NAME + "//executePackage";
		public static final String PACKAGE_UPLOADER = MODULE_NAME + "/uploadPackageSources";
		public static final String PACKAGE_UPLOAD_RUNNER_INFO = MODULE_NAME + "/getPackageUploadAndRunnerInfo?requestType={requestType}";
		
	}

}
