package com.anvizent.schedulers.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anvizent.schedulers.security.JwtUser;
import com.anvizent.schedulers.security.JwtUserFactory;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.User;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserDao userDao;
	
	public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username, null);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
	}

	@Override
	public ClientJdbcInstance getClientJdbcInstance(String clientId) throws AnvizentCorewsException {
		return new ClientJdbcInstance( userDao.getClientDbDetails(clientId));
	}
	@Override
	public  ClientJdbcInstance getClientJdbcInstance(HttpServletRequest request) throws AnvizentCorewsException {
		String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
		return getClientJdbcInstance(clientIdfromHeader);
	}
	
	@Override
	public List<CloudClient> getActiveClientsList() {
		return userDao.getActiveClientsList();
	}

}
