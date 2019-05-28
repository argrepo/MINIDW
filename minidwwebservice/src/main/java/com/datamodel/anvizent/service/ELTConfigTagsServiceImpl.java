package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.ELTConfigTagsDao;
import com.datamodel.anvizent.service.dao.ELTJobTagsDao;
import com.datamodel.anvizent.service.model.ELTConfigTags;

@Service
public class ELTConfigTagsServiceImpl implements ELTConfigTagsService{

	@Autowired
	ELTConfigTagsDao eltConfigDao;
	
	
	
	@Override
	public int saveEltStgKeyConfig(List<ELTConfigTags> eltStgKeyConfig, JdbcTemplate commonJdbcTemplate) {
		int create = 0;
		for(ELTConfigTags eltStgKeyConfigData : eltStgKeyConfig){
			if(eltStgKeyConfigData.getId() != null && eltStgKeyConfigData.getId() != 0){
				create = eltConfigDao.updateEltStgKeyConfig(eltStgKeyConfigData,commonJdbcTemplate);
			}else{
				create = eltConfigDao.saveEltStgKeyConfig(eltStgKeyConfigData,commonJdbcTemplate);
			}
			
		}
		
		return create;
	}

	@Override
	public int updateEltStgKeyConfig(ELTConfigTags eltStgKeyConfig, JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.updateEltStgKeyConfig(eltStgKeyConfig,commonJdbcTemplate);
	}

	@Override
	public List<ELTConfigTags> getELTStgConfigInfo(JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.getELTStgConfigInfo(commonJdbcTemplate);
	}

	@Override
	public int deleteEltStgKeyConfigById(Integer id, JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.deleteEltStgKeyConfigById(id,commonJdbcTemplate);
	}


	@Override
	public List<ELTConfigTags> getEltConfigTags(JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.getEltConfigTags(commonJdbcTemplate);
	}

	@Override
	public long saveEltConfigPairInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate) {
		long tagId = 0;
		if(eltilConfigs.getTagId() == null || eltilConfigs.getTagId() == 0){
			tagId = eltConfigDao.saveEltConfigPairInfo(eltilConfigs,commonJdbcTemplate);	
		}else{
			eltConfigDao.updateEltConfigTags(eltilConfigs,commonJdbcTemplate);
			tagId = eltilConfigs.getTagId();
		}
		
		return tagId;
	}

	@Override
	public List<ELTConfigTags> getEltConfigByTagId(Integer tagId, JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.getEltConfigByTagId(tagId,commonJdbcTemplate);
	}

	@Override
	public ELTConfigTags getEltConfigTagsByID(long id, JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.getEltConfigTagsByID(id,commonJdbcTemplate);
	}

	@Override
	public int deleteALLEltStgKeyConfigById(Integer tagId, JdbcTemplate commonJdbcTemplate) {
		return eltConfigDao.deleteALLEltStgKeyConfigById(tagId,commonJdbcTemplate);
	}

	@Override
	public long saveEltCloneTagInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate) {
		long id=0;
		id = eltConfigDao.saveEltCloneTagInfo(eltilConfigs,commonJdbcTemplate);
		eltConfigDao.saveEltCloneTagKeyPairInfo(id,eltilConfigs.getTagId(),commonJdbcTemplate);
		return id;
	}

	@Override
	public void saveELTKeyValuePairs(long tagId, List<Map<String, Object>> valuesList, JdbcTemplate commonJdbcTemplate) {
		eltConfigDao.saveELTKeyValuePairs(tagId,valuesList,commonJdbcTemplate);
	}

	

	

}
