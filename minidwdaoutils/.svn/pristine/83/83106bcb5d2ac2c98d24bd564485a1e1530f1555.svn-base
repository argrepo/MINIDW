package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;

public class EltJobInfoMapper implements RowMapper<EltJobInfo> {

	@Override
	public EltJobInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return getResultSetData(rs);
	}

	public EltJobInfo getResultSetData(ResultSet rs) throws SQLException{
		EltJobInfo eltJobInfo = new EltJobInfo();
		eltJobInfo.setId(rs.getLong("mappingid"));
		eltJobInfo.setJobName(rs.getString("job_name"));
		eltJobInfo.setActiveStatus(rs.getBoolean("isactive"));
		eltJobInfo.setConfigProp(new ELTConfigTags(rs.getLong("config_prop_tag")));
		eltJobInfo.getConfigProp().setTagName(rs.getString("config_prop_tag_name"));
		
		eltJobInfo.setValuesProp(new ELTConfigTags(rs.getLong("values_prop_tag")));
		eltJobInfo.getValuesProp().setTagName(rs.getString("values_prop_tag_name"));
		
		eltJobInfo.setStatsProp(new ELTConfigTags(rs.getLong("stats_prop_tag")));
		eltJobInfo.getStatsProp().setTagName(rs.getString("stats_prop_tag_name"));
		/*String derivedComponent = rs.getString("derived_component_config_id");
		if (derivedComponent != null) {
			String[] derivedComponentList = StringUtils.split(derivedComponent, ",");
			List<ELTKeyConfig> derivedComponent2 = eltJobInfo.getDerivedComponent();
			for (String component : derivedComponentList) {
				derivedComponent2.add(new ELTKeyConfig(Long.parseLong(component)));
			}
		}*/
		return eltJobInfo;
	}

}
