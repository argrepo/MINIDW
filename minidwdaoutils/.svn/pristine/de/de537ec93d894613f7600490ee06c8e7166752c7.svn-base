package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.EltJobTagInfo;

public class EltJobTagInfoMapper implements RowMapper<EltJobTagInfo> {

	@Override
	public EltJobTagInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		EltJobTagInfo eltJobTagInfo = new EltJobTagInfo();
		eltJobTagInfo.setTagId(rs.getLong("tag_id"));
		eltJobTagInfo.setTagName(rs.getString("tag_name"));
		eltJobTagInfo.getGlobalValues().setTagId(rs.getLong("global_values"));
		eltJobTagInfo.setActive(rs.getBoolean("is_active"));
		return eltJobTagInfo;
	}
}
