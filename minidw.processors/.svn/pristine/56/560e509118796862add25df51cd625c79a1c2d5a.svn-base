package com.anvizent.minidw.service.utils.processor;

import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.ILConnectionMapping;

public abstract class SourceUploadProcessor {
	
	public abstract void processFileUpload(int dlId,int clientId,String userId);
	
	public abstract void processFileUpload(DLInfo dlInfo,int clientId,String userId);
	
	public abstract void updateFlatFileMappingInfo(ILConnectionMapping ilConnectionMapping,int clientId,String userId);
	
	public abstract void generateCsvWithDbConnection(ILConnectionMapping ilConnectionMapping,int clientId,String userId);
	
	public abstract void generateCsvWithWebServiceConnection(ILConnectionMapping ilConnectionMapping,int clientId,String userId);
	
}
