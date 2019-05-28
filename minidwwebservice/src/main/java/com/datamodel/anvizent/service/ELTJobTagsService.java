package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;

public interface ELTJobTagsService {

	List<EltJobTagInfo> getEltJobTagList(JdbcTemplate commonJdbcTemplate);
	
	EltJobTagInfo getEltJobTagInfoById(long tagId,JdbcTemplate commonJdbcTemplate);
	
	int saveEltJobTagInfo(EltJobTagInfo eltJobTagInfo,JdbcTemplate commonJdbcTemplate);
	
	void deleteEltJobTagInfo(long tagId,JdbcTemplate commonJdbcTemplate);

	int saveEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate);

	void deleteDerivedByMappingId(long derivedId, JdbcTemplate commonJdbcTemplate);

	int updateEltJobSequenceInfo(List<EltJobInfo> eltJobInfo, JdbcTemplate commonJdbcTemplate);


	
}
