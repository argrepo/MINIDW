package com.datamodel.anvizent.service.model;

import java.util.List;
/**
 * 
 * @author rakesh.gajula
 *
 */
public class ClientData {
	private String userId;
	private String activationKey;
	private List<Industry> industries;
	private Package userPackage;
	private ILConnectionMapping ilConnectionMapping;
	private Schedule schedule;
	private ClientServerScheduler clientServerScheduler;
	private Modification modification;
	private boolean is_flat_file_locked;
	private boolean is_database_locked;
	private DLInfo dlInfo;
	private ILInfo ilInfo;
	
	 
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public List<Industry> getIndustries() {
		return industries;
	}

	public void setIndustries(List<Industry> industries) {
		this.industries = industries;
	}

	public Package getUserPackage() {
		return userPackage;
	}

	public void setUserPackage(Package userPackage) {
		this.userPackage = userPackage;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	public ILConnectionMapping getIlConnectionMapping() {
		return ilConnectionMapping;
	}

	public void setIlConnectionMapping(ILConnectionMapping ilConnectionMapping) {
		this.ilConnectionMapping = ilConnectionMapping;
	}

	public ClientServerScheduler getClientServerScheduler() {
		return clientServerScheduler;
	}

	public void setClientServerScheduler(ClientServerScheduler clientServerScheduler) {
		this.clientServerScheduler = clientServerScheduler;
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
	
	public DLInfo getDlInfo() {
		return dlInfo;
	}

	public void setDlInfo(DLInfo dlInfo) {
		this.dlInfo = dlInfo;
	}

	public ILInfo getIlInfo() {
		return ilInfo;
	}

	public void setIlInfo(ILInfo ilInfo) {
		this.ilInfo = ilInfo;
	}

	@Override
	public String toString() {
		return "ClientData [userId=" + userId + ", activationKey=" + activationKey + ", industries=" + industries
				+ ", userPackage=" + userPackage + ", ilConnectionMapping=" + ilConnectionMapping + ", schedule="
				+ schedule + ", clientServerScheduler=" + clientServerScheduler + ", modification=" + modification
				+ ", is_flat_file_locked=" + is_flat_file_locked + ", is_database_locked=" + is_database_locked + ", dlInfo=" + dlInfo + ", ilInfo=" + ilInfo + "]";
	}
	
}
