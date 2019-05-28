package com.anvizent.packagerunner.process;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.DataBaseProcessor;
import com.anvizent.minidw.service.utils.processor.FlatFileProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.ParseErrorMessage;
import com.anvizent.minidw.service.utils.processor.WebServiceProcessor;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;

@Component
public class ScheduleUploadProcessor {

	protected static final Log log = LogFactory.getLog(ScheduleUploadProcessor.class);

	@Autowired
	UserDao userDao;
	@Autowired
	PackageDao packageDao;
	@Autowired
	FileDao fileDao;
	@Autowired
	ScheduleDao scheduleDao;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	StandardPackageDao standardPackageDao;
	@Autowired
	FlatFileProcessor flatFileProcessor;
	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	DataBaseProcessor dbProcessor;
	@Autowired
	WebServiceProcessor webServiceProcessor;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	ParseErrorMessage parseErrorMessage;

	public void uploadDlSources(User user, String deploymentType, Package userPackage,
			PackageExecution packageExecution, CustomRequest customRequest, S3BucketInfo s3BucketInfo,
			FileSettings fileSettings) {

		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(
					userDao.getClientDbDetails(packageExecution.getClientId()));
			JdbcTemplate clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<ILConnectionMapping> ilConnectionMappings = standardPackageDao.getILConnectionMappingInfoByDLId(
					user.getUserId(), user.getClientId(), packageExecution.getDlId(), clientAppDbJdbcTemplate, true);
			ilConnectionMappings = getDecryptILConnectionMappingList(ilConnectionMappings);
			for (ILConnectionMapping ilMappingInfo : ilConnectionMappings) {
				if (!ilMappingInfo.getIsFlatFile() && !ilMappingInfo.getIsWebservice()) {
					dbProcessor.processDatabase(user, deploymentType, packageExecution, s3BucketInfo, fileSettings,
							customRequest, ilMappingInfo);
				} else if (ilMappingInfo.getIsFlatFile()) {
					flatFileProcessor.processFlatFile(user, packageExecution, ilMappingInfo, customRequest);
				} else if (ilMappingInfo.getIsWebservice()) {
					webServiceProcessor.processWebservice(user, userPackage, deploymentType, packageExecution,
							s3BucketInfo, fileSettings, ilMappingInfo, customRequest);
				}
			}
		} catch (Exception e) {
			throw new PackageExecutionException(e);
		}
	}

	public List<ILConnectionMapping> getDecryptILConnectionMappingList(
			List<ILConnectionMapping> ilConnectionMappingList) throws Exception {
		MinidwServiceUtil.getILConnectionMapping(ilConnectionMappingList);
		return ilConnectionMappingList;
	}

}
