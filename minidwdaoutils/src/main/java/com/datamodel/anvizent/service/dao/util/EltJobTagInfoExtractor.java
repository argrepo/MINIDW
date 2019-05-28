package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.datamodel.anvizent.service.model.EltJobTagInfo;

public class EltJobTagInfoExtractor implements ResultSetExtractor<EltJobTagInfo> {
	
	@Override
	public EltJobTagInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			EltJobTagInfo eltJobTagInfo = new EltJobTagInfo();
			eltJobTagInfo.setTagId(rs.getLong("tag_id"));
			eltJobTagInfo.setTagName(rs.getString("tag_name"));
			eltJobTagInfo.getGlobalValues().setTagId(rs.getLong("global_values"));
			eltJobTagInfo.setActive(rs.getBoolean("is_active"));
			return eltJobTagInfo;
		}
		return null;
	}

}
