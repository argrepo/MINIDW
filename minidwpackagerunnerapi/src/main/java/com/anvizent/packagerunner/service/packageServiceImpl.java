package com.anvizent.packagerunner.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anvizent.packagerunner.controller.RunPackage;
import com.anvizent.packagerunner.model.SchedulerInfo;


@Service("packageService")
public class packageServiceImpl implements PackageService{
	
	@Autowired
	public RunPackage runPackage;
	

	@Override
	public boolean runPackages(SchedulerInfo schd) {
		return runPackage.runPackages(schd);
	}


	@Override
	public Map<Long, SchedulerInfo> getPackagesList() {
		// TODO Auto-generated method stub
		return runPackage.getPackagesList();
	}
	
	@Override
	public SchedulerInfo getScheduleInfo(long id) {
		// TODO Auto-generated method stub
		return runPackage.getScheduleInfo(id);
	}


	@Override
	public int getMapSize() {
		// TODO Auto-generated method stub
		return runPackage.getMapSize();
	}


	@Override
	public SchedulerInfo getScheduleRemoveId(long id) {
		// TODO Auto-generated method stub
		return runPackage.getScheduleRemoveId(id);
	}

}
