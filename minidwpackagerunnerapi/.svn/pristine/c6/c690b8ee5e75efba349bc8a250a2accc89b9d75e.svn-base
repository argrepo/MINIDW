package com.anvizent.packagerunner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anvizent.packagerunner.security.JwtUser;
import com.anvizent.packagerunner.security.JwtUserFactory;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.User;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	JdbcTemplate clientAppDbJdbcTemplate = null;

	public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUser(username,clientAppDbJdbcTemplate);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
	}

}
