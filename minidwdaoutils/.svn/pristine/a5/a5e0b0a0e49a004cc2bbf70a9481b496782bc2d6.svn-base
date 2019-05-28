package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.Modification;

public class HierarchicalExtracter implements ResultSetExtractor<Hierarchical>{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Hierarchical extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			return getMasterConfig(rs);
		}
		return null;
	}

	public Hierarchical getMasterConfig(ResultSet rs) throws SQLException {
		Hierarchical hierarchical = new Hierarchical();
		hierarchical.setId(rs.getLong("id"));
		hierarchical.setName(rs.getString("name"));
		hierarchical.setDescription(rs.getString("description"));
		hierarchical.setHierarchicalFormData(rs.getString("hierarchical_form_data"));
		hierarchical.setHierarchicalLevelData(rs.getString("hierarchical_level_data"));
		hierarchical.setActive(rs.getBoolean("is_active"));
		Modification modification = new Modification();
		modification.setCreatedBy(rs.getString("created_by"));
		modification.setCreatedTime(rs.getString("created_time"));
		modification.setModifiedBy(rs.getString("modified_by"));
		modification.setModifiedTime(rs.getString("modified_date"));
		hierarchical.setModification(modification);
		return hierarchical;
	}

}
