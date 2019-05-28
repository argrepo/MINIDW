/**
 * 
 */
package com.datamodel.anvizent.service.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface MasterDao {

	Map<String, String> getJobExecutionLimitByClient(String clientId, JdbcTemplate jdbcTemplate);
	 //SchedugetSchedulerMasterInfo(long masterId)
}
