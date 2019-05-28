package com.anvizent.schedulers.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.anvizent.schedulers.security.JwtUser;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.service.model.CloudClient;



public interface UserService {
	
	JwtUser loadUserByUsername(String name);

	ClientJdbcInstance getClientJdbcInstance(String clientId) throws AnvizentCorewsException;
	
	ClientJdbcInstance getClientJdbcInstance(HttpServletRequest request) throws AnvizentCorewsException;

	List<CloudClient> getActiveClientsList();
	
}
