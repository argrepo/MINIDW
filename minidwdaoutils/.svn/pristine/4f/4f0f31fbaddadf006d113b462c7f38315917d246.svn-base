package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.JobResult;

public interface HierarchicalDao {

	List<Hierarchical> fetchAllHierarchicalList(JdbcTemplate commonJdbcTemplate);

	Hierarchical fetchHierarchicalById(long id, JdbcTemplate commonJdbcTemplate);

	long addHierarchical(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate);

	long updateHierarchical(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate);

	void deleteHierarchicalById(long id, JdbcTemplate commonJdbcTemplate);

	long addHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate);

	long updateHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate);

	Hierarchical fetchHierarchicalAssociationByConfigId(long configId, JdbcTemplate commonJdbcTemplate);

	Hierarchical fetachHierarchicalAndMappingInfo(long configId, JdbcTemplate commonJdbcTemplate);

	List<JobResult> getJobResultsForHierarchical(Integer hierarchicalId, String hierarchicalName, String clientId,
			String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForHierarchicalByDate(Integer hierarchicalId, String hierarchicalName, String clientId,
			String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValues(String columnName, String tableName, JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValuesByRange(String columnName, String tableName, String fromRange, String toRange,
			JdbcTemplate clientJdbcTemplate);

	List<Object> getDistinctValuesByPattern(String columnName, String tableName, String patternValue,
			String patternRangeValue, JdbcTemplate clientJdbcTemplate);
	
}
