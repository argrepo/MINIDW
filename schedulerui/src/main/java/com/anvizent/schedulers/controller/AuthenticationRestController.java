package com.anvizent.schedulers.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.schedulers.model.JwtAuthenticationResponse;
import com.anvizent.schedulers.security.JwtAuthenticationRequest;
import com.anvizent.schedulers.security.JwtTokenUtil;
import com.anvizent.schedulers.security.JwtUser;
import com.anvizent.schedulers.service.UserService;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

@RestController

public class AuthenticationRestController {

	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
    private String tokenHeader = "authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;
	RestTemplate restTemplate = new RestTemplate();
	
	/* 
	  * Perform the security and Reload password post-security so we can generate token
    */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<DataResponse> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request)
	{

		Device device = new LiteDeviceResolver().resolveDevice(request);
		JwtUser userDetails = null;
		HttpStatus status = HttpStatus.OK;
		String token = null;
		Message message = new Message();
		message.setCode("ERROR");
		try
		{
			String encryptedClientId = AESConverter.encrypt("-1");
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("X-Auth-Client-Token", encryptedClientId);
			HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(httpHeaders);

			String authEndPoint = authenticationEndPointUrl + "/authenticateUser/{username}/{password}/{clientID}";
			ResponseEntity<?> authDataResponseEntity = restTemplate.exchange(authEndPoint, HttpMethod.POST, headerParamsPost, Object.class, authenticationRequest.getUsername(), authenticationRequest.getPassword(), "-1");

			@SuppressWarnings("unchecked")
			Map<String, Object> authDataResponse = (Map<String, Object>) authDataResponseEntity.getBody();

			if( authDataResponse != null )
			{
				String authStatus = (String) authDataResponse.get("status");
				if( authStatus.equalsIgnoreCase("success") && authDataResponse.get("roleid") != null )
				{
					Integer roleId = Integer.valueOf(authDataResponse.get("roleid").toString());
					if( roleId != null && roleId == -200 )
					{
						userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
					}
					else
					{
						throw new IllegalArgumentException("");
					}
				}
				else
				{
					throw new IllegalArgumentException("");
				}
			}
			else
			{
				throw new UsernameNotFoundException("not a valid user.");
			}
		}
		catch ( UsernameNotFoundException e )
		{
			message.setText("User Details not found");
		}
		catch ( IllegalArgumentException e )
		{
			message.setText("not a valid user.");
		}
		catch ( Throwable t )
		{
			t.printStackTrace();
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		if( userDetails != null )
		{
			token = jwtTokenUtil.generateToken(userDetails, device);
			message.setCode("SUCCESS");
		}

		DataResponse dataResponse = new DataResponse();
		dataResponse.addMessage(message);
		dataResponse.setObject(token);
		return new ResponseEntity<DataResponse>(dataResponse, status);
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
