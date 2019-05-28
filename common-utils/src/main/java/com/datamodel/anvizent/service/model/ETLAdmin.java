/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.util.List;

/**
 * @author rakesh.gajula
 *
 */
public class ETLAdmin {
	
	private String clientId;
	private Integer dlId;
	private Modification modification;
	private List<ILInfo> iLInfo;
	private ILInfo ilInfo;
	private List<String>  fileNames;
	private List<ETLJobContextParam>  eTLJobContextParamList;
    private DLInfo dlInfo;
    private List<DLInfo> dLInfo;
    private List<ILConnectionMapping> iLConnectionMapping;
	private ILConnectionMapping ilConnectionMapping;
	private boolean is_flat_file_locked;
	private boolean is_database_locked;
	private Integer maxFileSizeInMB;
	private List<Kpi> kpiInfo;
	
	public DLInfo getDlInfo() {
		return dlInfo;
	}
	public void setDlInfo(DLInfo dlInfo) {
		this.dlInfo = dlInfo;
	}
	public List<DLInfo> getdLInfo() {
		return dLInfo;
	}
	public void setdLInfo(List<DLInfo> dLInfo) {
		this.dLInfo = dLInfo;
	}
	public List<ETLJobContextParam> geteTLJobContextParamList() {
		return eTLJobContextParamList;
	}
	public void seteTLJobContextParamList(List<ETLJobContextParam> eTLJobContextParamList) {
		this.eTLJobContextParamList = eTLJobContextParamList;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	public ILInfo getIlInfo() {
		return ilInfo;
	}
	public void setIlInfo(ILInfo ilInfo) {
		this.ilInfo = ilInfo;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public List<ILInfo> getiLInfo() {
		return iLInfo;
	}
	public void setiLInfo(List<ILInfo> iLInfo) {
		this.iLInfo = iLInfo;
	}
	public Integer getDlId() {
		return dlId;
	}
	public void setDlId(Integer dlId) {
		this.dlId = dlId;
	}	
	public List<ILConnectionMapping> getiLConnectionMapping() {
		return iLConnectionMapping;
	}
	public void setiLConnectionMapping(List<ILConnectionMapping> iLConnectionMapping) {
		this.iLConnectionMapping = iLConnectionMapping;
	}
    public ILConnectionMapping getIlConnectionMapping() {
		return ilConnectionMapping;
	}
	public void setIlConnectionMapping(ILConnectionMapping ilConnectionMapping) {
		this.ilConnectionMapping = ilConnectionMapping;
	}
	public boolean getIs_flat_file_locked() {
		return is_flat_file_locked;
	}
	public void setIs_flat_file_locked(boolean is_flat_file_locked) {
		this.is_flat_file_locked = is_flat_file_locked;
	}
	public boolean getIs_database_locked() {
		return is_database_locked;
	}
	public void setIs_database_locked(boolean is_database_locked) {
		this.is_database_locked = is_database_locked;
	} 
	
	public Integer getMaxFileSizeInMB() {
		return maxFileSizeInMB;
	}
	public void setMaxFileSizeInMB(Integer maxFileSizeInMB) {
		this.maxFileSizeInMB = maxFileSizeInMB;
	}
	
	public List<Kpi> getKpiInfo() {
		return kpiInfo;
	}
	public void setKpiInfo(List<Kpi> kpiInfo) {
		this.kpiInfo = kpiInfo;
	}
	/*
	public List<MultipartFile> getJarFileNames() {
		return jarFileNames;
	}
	public void setJarFileNames(List<MultipartFile> jarFileNames) {
		this.jarFileNames = jarFileNames;
	}*/
	
	@Override
	public String toString() {
		return "ETLAdmin [clientId=" + clientId + ", dlId=" + dlId + ", modification=" + modification + ", iLInfo="
				+ iLInfo + ", ilInfo=" + ilInfo + ", fileNames=" + fileNames + ", eTLJobContextParamList="
				+ eTLJobContextParamList + ", dlInfo=" + dlInfo + ", dLInfo=" + dLInfo + ", iLConnectionMapping="
				+ iLConnectionMapping + ", ilConnectionMapping=" + ilConnectionMapping + ", is_flat_file_locked="
				+ is_flat_file_locked + ", is_database_locked=" + is_database_locked + ", maxFileSizeInMB="
				+ maxFileSizeInMB + ", kpiInfo=" + kpiInfo + "]";
	}
	
	/*@Override
	public String toString() {
		return "ETLAdmin [clientId=" + clientId + ", dlId=" + dlId + ", modification=" + modification + ", iLInfo="
				+ iLInfo + ", ilInfo=" + ilInfo + ", fileNames=" + fileNames + ", eTLJobContextParamList="
				+ eTLJobContextParamList + ", dlInfo=" + dlInfo + ", dLInfo=" + dLInfo + ", iLConnectionMapping="
				+ iLConnectionMapping + ", ilConnectionMapping=" + ilConnectionMapping + ", is_flat_file_locked="
				+ is_flat_file_locked + ", is_database_locked=" + is_database_locked + ", maxFileSizeInMB="
				+ maxFileSizeInMB + ", kpiInfo=" + kpiInfo + ", jarFileNames=" + jarFileNames + "]";
	}*/
		
}
