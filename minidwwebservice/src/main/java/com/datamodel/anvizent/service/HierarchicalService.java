package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.User;

public interface HierarchicalService {

	List<Hierarchical> getAllHierarchicalList(JdbcTemplate commonJdbcTemplate);

	Hierarchical getHierarchicalById(long id, JdbcTemplate commonJdbcTemplate);

	long saveHierarchical(Hierarchical eltMasterConfiguration, JdbcTemplate commonJdbcTemplate);

	Hierarchical deleteHierarchicalById(long id, JdbcTemplate commonJdbcTemplate);

	long saveHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate);

	Hierarchical getHierarchicalAssociationByConfigId(long configId, JdbcTemplate commonJdbcTemplate);

	Hierarchical getHierarchicalAndMappingInfoById(long configId, JdbcTemplate commonJdbcTemplate);

	void processHierarchicalMappingJob(Hierarchical hierarchical, User user, Map<String, Object> clientDbCredentials,
			JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, CustomRequest customRequest);

	void processHierarchicalJob(Hierarchical hierarchical, User user, Map<String, Object> clientDbCredentials,
			JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, CustomRequest customRequest);

	List<JobResult> getJobResultsForHierarchical(Integer hierarchicalId, String hierarchicalName,
			String clientIdfromHeader, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForHierarchicalByDate(Integer hierarchicalId, String hierarchicalName,
			String clientIdfromHeader, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValues(String columnName, String tableName, JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValuesByRange(String columnName, String getiL_table_name, String fromRange, String toRange,
			JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValuesByPattern(String columnName, String getiL_table_name, String patternValue,
			String patternRangeValue, JdbcTemplate clientJdbcTemplate);

}
