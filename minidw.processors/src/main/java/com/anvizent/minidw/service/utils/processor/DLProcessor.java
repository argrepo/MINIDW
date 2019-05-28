package com.anvizent.minidw.service.utils.processor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.User;

@Component
public class DLProcessor {
	protected static final Log logger = LogFactory.getLog(ILProcessor.class);

	public DLProcessor() {
		logger.info("In DLProcessor...");
	}

	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	CommonProcessor commonProcessor;

	/**
	 * @param user
	 * @param dLId
	 * @param packageId
	 * @param packageExecution
	 * @param customRequest
	 * @return
	 * @throws PackageExecutionException
	 */
	public Map<String, Object> processDL(int dLId, Integer packageId, PackageExecution packageExecution,
			CustomRequest customRequest) throws PackageExecutionException {

		Map<String, Object> responseMap = null;
		MultiValueMap<Object, Object> dlMap = new LinkedMultiValueMap<>();
		dlMap.add("dLId", dLId);
		dlMap.add("packageId", packageId);
		dlMap.add("executionId", packageExecution.getExecutionId());
		dlMap.add("timeZone", packageExecution.getTimeZone());
		responseMap = metaDataFetch.runDl(dlMap, packageExecution, customRequest);
		return responseMap;
	}

	/**
	 * @param user
	 * @param packageId
	 * @param ddLTableSet
	 * @param ddLTableNamesSet
	 * @param packageExecution
	 * @param customRequest
	 */
	public List<String> processCustomTableSets(Integer packageId, Set<Integer> ddLTableSet,
			Set<String> ddLTableNamesSet, PackageExecution packageExecution, CustomRequest customRequest) {

		//List<String> totalTablesList = new ArrayList<>(ddLTableNamesSet);
		List<String> totalTableNamesList = new ArrayList<>();
		if (ddLTableSet != null && ddLTableSet.size() > 0) {
			totalTableNamesList = metaDataFetch.runDDlayouts(ddLTableSet, customRequest);
		}
		return totalTableNamesList;
	}

}
