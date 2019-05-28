package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.Modification;

public class HierarchicalMapper implements RowMapper<Hierarchical> {

	@Override
	public Hierarchical mapRow(ResultSet rs, int rowNum) throws SQLException {
		return getMasterConfig(rs);
	}
	public Hierarchical getMasterConfig(ResultSet rs) throws SQLException {
		Hierarchical hierarchical = new Hierarchical();
		hierarchical.setId(rs.getLong("id"));
		hierarchical.setName(rs.getString("name"));
		hierarchical.setDescription(rs.getString("description"));
		hierarchical.setHierarchicalFormData(rs.getString("hierarchical_form_data"));
		hierarchical.setActive(rs.getBoolean("is_active"));
		hierarchical.setHierarchicalLevelData(rs.getString("hierarchical_level_data"));
		Modification modification = new Modification();
		modification.setCreatedBy(rs.getString("created_by"));
		modification.setCreatedTime(rs.getString("created_time"));
		modification.setModifiedBy(rs.getString("modified_by"));
		modification.setModifiedTime(rs.getString("modified_date"));
		hierarchical.setModification(modification);
		return hierarchical;
	}
	
}
