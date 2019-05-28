package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;

public interface ELTJobTagsDao {

	
	List<EltJobTagInfo> getEltJobTagList(JdbcTemplate commonJdbcTemplate);
	
	EltJobTagInfo getEltJobTagInfoById(long tagId,JdbcTemplate commonJdbcTemplate);
	
	
	int addEltJobTagInfo(EltJobTagInfo eltJobTagInfo,JdbcTemplate commonJdbcTemplate);
	
	int updateEltJobTagInfo(EltJobTagInfo eltJobTagInfo,JdbcTemplate commonJdbcTemplate);
	
	void deleteEltJobTagInfo(long tagId,JdbcTemplate commonJdbcTemplate);

	long saveEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate);

	int saveEltDerivedComponentInfo(long id, ELTConfigTags tagIds, JdbcTemplate commonJdbcTemplate);

	int updateEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate);


	void deleteDerivedByMappingId(long derivedId, JdbcTemplate commonJdbcTemplate);

	public List<ELTConfigTags> getEltJobDerivedMappingInfo(long jobId, JdbcTemplate commonJdbcTemplate);

	int updateEltJobSequenceInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate);




}
