package com.datamodel.anvizent.service.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.datamodel.anvizent.helper.minidw.Constants;

public class SaveProperties {

	public static void saveAccessKey(String accessKey) throws FileNotFoundException, IOException {
		File minidwConfiFile = new File(Constants.Config.MINIDW_SERVICE_CONFIG);
		Properties endPointProperties = new Properties();  
		endPointProperties.load(new FileInputStream(minidwConfiFile));
		endPointProperties.setProperty(Constants.Config.ACCESS_KEY, accessKey);
		endPointProperties.store(new FileOutputStream(minidwConfiFile), "");
	}


}
