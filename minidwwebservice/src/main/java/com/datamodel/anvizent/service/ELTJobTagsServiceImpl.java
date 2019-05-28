package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.ELTJobTagsDao;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;

@Service
public class ELTJobTagsServiceImpl implements ELTJobTagsService{

	@Autowired
	ELTJobTagsDao eltDao;
	
	
	@Override
	public List<EltJobTagInfo> getEltJobTagList(JdbcTemplate commonJdbcTemplate) {
		return eltDao.getEltJobTagList(commonJdbcTemplate);
	}

	@Override
	public EltJobTagInfo getEltJobTagInfoById(long tagId, JdbcTemplate commonJdbcTemplate) {
		return eltDao.getEltJobTagInfoById(tagId, commonJdbcTemplate);
	}

	@Override
	public int saveEltJobTagInfo(EltJobTagInfo eltJobTagInfo, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		if ( eltJobTagInfo.getTagId() != null && eltJobTagInfo.getTagId() != 0 ) {
			eltDao.updateEltJobTagInfo(eltJobTagInfo, commonJdbcTemplate);
			id = eltJobTagInfo.getTagId();
		} else {
			id = eltDao.addEltJobTagInfo(eltJobTagInfo, commonJdbcTemplate);
		}
		return (int) id;
	}

	@Override
	public void deleteEltJobTagInfo(long tagId, JdbcTemplate commonJdbcTemplate) {
		eltDao.deleteEltJobTagInfo(tagId, commonJdbcTemplate);
	}

	@Override
	public int saveEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		if(eltJobInfo.getId() == null || eltJobInfo.getId() == 0){
			id = eltDao.saveEltJobMappingInfo(eltJobInfo,commonJdbcTemplate);
		}else{
			eltDao.updateEltJobMappingInfo(eltJobInfo,commonJdbcTemplate);
			id = eltJobInfo.getId();
			eltDao.deleteDerivedByMappingId(eltJobInfo.getId(), commonJdbcTemplate);
		}
		if(eltJobInfo.getDerivedComponent() !=null && eltJobInfo.getDerivedComponent().size() > 0){
			for(ELTConfigTags tagIds : eltJobInfo.getDerivedComponent()){
				eltDao.saveEltDerivedComponentInfo(id,tagIds,commonJdbcTemplate);		
			}
		}
		
		return (int) id;
		
	}
	

	@Override
	public void deleteDerivedByMappingId(long derivedId, JdbcTemplate commonJdbcTemplate) {
		eltDao.deleteDerivedByMappingId(derivedId, commonJdbcTemplate);
	}


	@Override
	public int updateEltJobSequenceInfo(List<EltJobInfo> eltJobInfo, JdbcTemplate commonJdbcTemplate) {
		int update=0;
		for(EltJobInfo eltJobInfoSeq : eltJobInfo){
			if(eltJobInfoSeq.getId() != null || eltJobInfoSeq.getId() != 0){
				update =  eltDao.updateEltJobSequenceInfo(eltJobInfoSeq,commonJdbcTemplate);
			}
		}
		return update;
	}



	

	

}
