package com.anvizent.packagerunner.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.packagerunner.model.JwtAuthenticationResponse;
import com.anvizent.packagerunner.security.JwtAuthenticationRequest;
import com.anvizent.packagerunner.security.JwtTokenUtil;
import com.anvizent.packagerunner.security.JwtUser;
import com.anvizent.packagerunner.service.UserService;

@RestController

public class AuthenticationRestController {

    private String tokenHeader = "authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) {

    	Device device = new LiteDeviceResolver().resolveDevice(request);
        // Perform the security
        // Reload password post-security so we can generate token
    	JwtUser userDetails = null;
    	HttpStatus status = HttpStatus.OK;
    	String token = null;
        try {
        	userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
		} catch (UsernameNotFoundException e) {
			status = HttpStatus.UNAUTHORIZED;
			//token = "{'statusText':'User Details not found'}";
		}
        if (userDetails != null) {
        	token = jwtTokenUtil.generateToken(userDetails, device);
        }

        // Return the token
        return new ResponseEntity<Object>(new JwtAuthenticationResponse(token), status);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = userService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
