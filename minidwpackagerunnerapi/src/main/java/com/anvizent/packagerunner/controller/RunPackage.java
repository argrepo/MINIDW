package com.anvizent.packagerunner.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.anvizent.packagerunner.model.SchedulerInfo;

@Component
public class RunPackage {
	private Map<Long, SchedulerInfo> packageMap = new HashMap<>();

	public boolean runPackages(SchedulerInfo schedular) {
		addToPackageList(schedular);
		return true;
	}

	public void addToPackageList(SchedulerInfo schd) {

		if (schd.getScheduleId() != null) {
			packageMap.put(schd.getScheduleId(), schd);
		}
	}

	public Map<Long, SchedulerInfo> getPackagesList() {
		return packageMap;
	}
	
	public SchedulerInfo getScheduleInfo(long id) {
		return packageMap.get(id);
	}

	public int getMapSize() {
		return packageMap.size();
	}
	
	public SchedulerInfo getScheduleRemoveId(long id) {
		return packageMap.remove(id);
	}

}
