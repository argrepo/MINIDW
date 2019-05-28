package com.datamodel.anvizent.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.HierarchicalDao;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.User;

@Service
public class HierarchicalServiceImpl implements HierarchicalService {
	
	protected static final Log LOGGER = LogFactory.getLog(HierarchicalServiceImpl.class);
	
	private @Value("${hierarchical.classname:local_project.il_hierarchy_main_v1_0_1.IL_Hierarchy_Main_v1}") String HIERARCHY_JOB_CLASS; 
	private @Value("${hierarchical.dependancyjars.list:il_hierarchy_main_v1_0_1.jar,il_hierarchy_v1_0_1.jar}") String HIERARCHY_DEPENDENCY_JARS;
	
	private @Value("${hierarchicalmapping.classname:local_project.dl_il_hierarchy_map_main_v1_0_1.DL_IL_Hierarchy_Map_Main_v1}") String HIERARCHY_MAPPING_JOB_CLASS; 
	private @Value("${hierarchicalmapping.dependancyjars.list:dl_il_hierarchy_map_main_v1_0_1.jar,dl_il_hierarchy_map_v1_0_1.jar}") String HIERARCHY_MAPPING_DEPENDENCY_JARS;

	@Autowired
	HierarchicalDao hierarchicalDao;
	@Autowired
	MetaDataFetch metaDataFetch;

	@Override
	public List<Hierarchical> getAllHierarchicalList(JdbcTemplate commonJdbcTemplate) {
		return hierarchicalDao.fetchAllHierarchicalList(commonJdbcTemplate);
	}

	@Override
	public Hierarchical getHierarchicalById(long id, JdbcTemplate commonJdbcTemplate) {
		
		Hierarchical hierarchicalById = hierarchicalDao.fetchHierarchicalById(id, commonJdbcTemplate);
		if (hierarchicalById == null) {
			throw new RuntimeException("Master Configuration not found with id " + id  );
		}
		return hierarchicalById;
	}

	@Override
	public long saveHierarchical(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long masterConfigId = 0;
		
		if (hierarchical.getId() == 0) {
			masterConfigId = hierarchicalDao.addHierarchical(hierarchical, commonJdbcTemplate);
		} else {
			hierarchicalDao.updateHierarchical(hierarchical, commonJdbcTemplate);
			masterConfigId = hierarchical.getId();
		}
		return masterConfigId;
	}

	@Override
	public Hierarchical deleteHierarchicalById(long id, JdbcTemplate commonJdbcTemplate) {
		Hierarchical hierarchicalById = getHierarchicalById(id, commonJdbcTemplate);
		hierarchicalDao.deleteHierarchicalById(id, commonJdbcTemplate);
		return hierarchicalById;
	}

	@Override
	public long saveHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long associationId = 0;
		  
		if (hierarchical.getAssociationId() == 0) {
			associationId = hierarchicalDao.addHierarchicalAssociation(hierarchical, commonJdbcTemplate);
		} else {
			//boolean precheckhierarchyIlMap = hierarchicalDao.checkHierarchyMapping(hierarchical, commonJdbcTemplate);
			hierarchicalDao.updateHierarchicalAssociation(hierarchical, commonJdbcTemplate);
			associationId = hierarchical.getId();
		}
		return associationId;		
	}

	@Override
	public Hierarchical getHierarchicalAssociationByConfigId(long configId, JdbcTemplate commonJdbcTemplate) {
		Hierarchical hierarchicalById = hierarchicalDao.fetchHierarchicalAssociationByConfigId(configId, commonJdbcTemplate);
		/*if (hierarchicalById == null) {
			throw new RuntimeException("Master Configuration not found with id " + configId  );
		}*/
		return hierarchicalById;
	}

	@Override
	public Hierarchical getHierarchicalAndMappingInfoById(long configId, JdbcTemplate commonJdbcTemplate) {
		return hierarchicalDao.fetachHierarchicalAndMappingInfo(configId, commonJdbcTemplate);
	}

	@Override
	public void processHierarchicalJob(Hierarchical hierarchical, User user, Map<String, Object> clientDbCredentials,
			JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, CustomRequest customRequest) {

		String dbHost = clientDbCredentials.get("region_hostname").toString();
		String dbPort = clientDbCredentials.get("region_port").toString();
		String dbUserName = clientDbCredentials.get("clientdb_username").toString();
		String dbPassword = clientDbCredentials.get("clientdb_password").toString();
		String mainSchemaName = clientDbCredentials.get("clientdb_schema").toString();
		String stagingSchemaName = clientDbCredentials.get("clientdb_staging_schema").toString();
		
		String tempPath = Constants.Temp.getTempFileDir();
		tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
		tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
		String jobFilesPath = tempPath + "JobFiles/";
		CommonUtils.createDir(jobFilesPath);

		Map<String, String> hierarchicalContextParams = MinidwServiceUtil.getHierarchicalJobContextParams(
				dbHost, dbPort, dbUserName, dbPassword, stagingSchemaName, mainSchemaName,	hierarchical, user.getClientId(), jobFilesPath);
		LOGGER.info(hierarchicalContextParams);
		JobExecutionInfo hierarchicalJobStatus =  MinidwServiceUtil.runHierarchicalJob(hierarchicalContextParams, hierarchical, HIERARCHY_JOB_CLASS, HIERARCHY_DEPENDENCY_JARS);
		if ( hierarchicalJobStatus.getStatusCode() == 1) {
			throw new RuntimeException(hierarchicalJobStatus.getExecutionMessages());
		}
	}

	@Override
	public void processHierarchicalMappingJob(Hierarchical hierarchical, User user, Map<String, Object> clientDbCredentials,
			JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, CustomRequest customRequest) {
		
		String dbHost = clientDbCredentials.get("region_hostname").toString();
		String dbPort = clientDbCredentials.get("region_port").toString();
		String dbUserName = clientDbCredentials.get("clientdb_username").toString();
		String dbPassword = clientDbCredentials.get("clientdb_password").toString();
		String mainSchemaName = clientDbCredentials.get("clientdb_schema").toString();
		String stagingSchemaName = clientDbCredentials.get("clientdb_staging_schema").toString();
		
		String tempPath = Constants.Temp.getTempFileDir();
		tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
		tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
		String jobFilesPath = tempPath + "JobFiles/";
		CommonUtils.createDir(jobFilesPath);
		
		Map<String, String> hierarchicalMappingContextParams = MinidwServiceUtil.getHierarchicalMappingJobContextParams(
				dbHost, dbPort, dbUserName, dbPassword, stagingSchemaName, mainSchemaName,	hierarchical, user.getClientId(), jobFilesPath);
		LOGGER.info(hierarchicalMappingContextParams);
		JobExecutionInfo hierarchicalJobStatus =  MinidwServiceUtil.runHierarchicalJob(hierarchicalMappingContextParams, hierarchical, HIERARCHY_MAPPING_JOB_CLASS, HIERARCHY_MAPPING_DEPENDENCY_JARS);
		 
		PackageExecution packageExecution = new PackageExecution();
		  packageExecution.setDerivedTablesList(Arrays.asList(hierarchicalMappingContextParams.get("DDL_NAME")));
		
		  metaDataFetch.reloadAnvizentTableAccessEndPoint(packageExecution, customRequest);
		
		if ( hierarchicalJobStatus.getStatusCode() == 1) {
			throw new RuntimeException(hierarchicalJobStatus.getExecutionMessages());
		}

	}

	@Override
	public List<JobResult> getJobResultsForHierarchical(Integer hierarchicalId, String hierarchicalName,
			String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate) {
		return hierarchicalDao.getJobResultsForHierarchical(hierarchicalId, hierarchicalName, clientId, clientStagingSchemaName, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResultsForHierarchicalByDate(Integer hierarchicalId, String hierarchicalName,
			String clientId, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		return hierarchicalDao.getJobResultsForHierarchicalByDate(hierarchicalId, hierarchicalName, clientId, clientStagingSchemaName, fromDate, toDate, clientJdbcTemplate);
	}

	@Override
	public List<Object> getDistinctValues(String columnName, String tableName, JdbcTemplate clientJdbcTemplate) {
		return hierarchicalDao.getDistinctValues(columnName, tableName, clientJdbcTemplate);
	}

	@Override
	public List<Object> getDistinctValuesByRange(String columnName, String tableName, String fromRange,
			String toRange, JdbcTemplate clientJdbcTemplate) {
		return hierarchicalDao.getDistinctValuesByRange(columnName, tableName, fromRange, toRange, clientJdbcTemplate);
	}

	@Override
	public List<Object> getDistinctValuesByPattern(String columnName, String tableName, String patternValue,
			String patternRangeValue, JdbcTemplate clientJdbcTemplate) {
		return hierarchicalDao.getDistinctValuesByPattern(columnName, tableName, patternValue, patternRangeValue, clientJdbcTemplate);
	}
	

}
