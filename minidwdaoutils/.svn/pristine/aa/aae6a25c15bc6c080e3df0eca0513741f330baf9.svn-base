package com.datamodel.anvizent.service.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;

public interface ELTConfigTagsDao {

	int saveEltStgKeyConfig(ELTConfigTags eltStgKeyConfig, JdbcTemplate commonJdbcTemplate);

	int updateEltStgKeyConfig(ELTConfigTags eltStgKeyConfig, JdbcTemplate commonJdbcTemplate);

	List<ELTConfigTags> getELTStgConfigInfo(JdbcTemplate commonJdbcTemplate);

	int deleteEltStgKeyConfigById(Integer id, JdbcTemplate commonJdbcTemplate);

	List<ELTConfigTags> getELTMainConfigInfo(JdbcTemplate commonJdbcTemplate);

	List<ELTConfigTags> getEltConfigTags(JdbcTemplate commonJdbcTemplate);

	long saveEltConfigPairInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate);

	List<ELTConfigTags> getEltConfigByTagId(Integer tagId, JdbcTemplate commonJdbcTemplate);
	
	public List<ELTConfigTags> getEltJobDerivedMappingInfo(long jobId, JdbcTemplate commonJdbcTemplate);
	
	int updateEltConfigTags(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate);

	ELTConfigTags getEltConfigTagsByID(long id, JdbcTemplate commonJdbcTemplate);

	int deleteALLEltStgKeyConfigById(Integer tagId, JdbcTemplate commonJdbcTemplate);

	long saveEltCloneTagInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate);

	void saveEltCloneTagKeyPairInfo(long id, Long tagId, JdbcTemplate commonJdbcTemplate);

	void saveELTKeyValuePairs(long tagId, List<Map<String, Object>> valuesList, JdbcTemplate commonJdbcTemplate);
	

}
