package com.datamodel.anvizent.service.model;

import java.util.ArrayList;
import java.util.List;

public class MasterDataValidation {
	
	private String clientId;
	private List<ILInfo> iLsInfo;
	private ILInfo ilInfo;
	private List<DLInfo> dlsInfo; 
	private DLInfo dlInfo; 
	private Modification modification;
	private List<DataValidation> dataValidations;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public List<ILInfo> getiLsInfo() {
		return iLsInfo;
	}
	public void setiLsInfo(List<ILInfo> iLsInfo) {
		this.iLsInfo = iLsInfo;
	}
	public ILInfo getIlInfo() {
		return ilInfo;
	}
	public void setIlInfo(ILInfo ilInfo) {
		this.ilInfo = ilInfo;
	}
	public List<DLInfo> getDlsInfo() {
		return dlsInfo;
	}
	public void setDlsInfo(List<DLInfo> dlsInfo) {
		this.dlsInfo = dlsInfo;
	}
	public DLInfo getDlInfo() {
		return dlInfo;
	}
	public void setDlInfo(DLInfo dlInfo) {
		this.dlInfo = dlInfo;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public List<DataValidation> getDataValidations() {
		return dataValidations;
	}
	public void setDataValidations(List<DataValidation> dataValidations) {
		this.dataValidations = dataValidations;
	}
	
	public void addDataValidation(DataValidation dataValidation){
		if(dataValidations == null){
			dataValidations = new ArrayList<DataValidation>();
		}
		dataValidations.add(dataValidation);
	}
	
	@Override
	public String toString() {
		return "MasterValidation [clientId=" + clientId + ", iLsInfo=" + iLsInfo + ", ilInfo=" + ilInfo + ", dlsInfo="
				+ dlsInfo + ", dlInfo=" + dlInfo + ", modification=" + modification + ", dataValidations="
				+ dataValidations + "]";
	}

}
