package minidwclientws;

import static com.anvizent.minidw.service.utils.helper.CommonUtils.sanitizeCsvForWsTempTable;
import static com.anvizent.minidw.service.utils.helper.WebServiceCSVWriter.collectHeaders;
import static com.anvizent.minidw.service.utils.helper.WebServiceCSVWriter.createTargetTable;
import static com.anvizent.minidw.service.utils.helper.WebServiceCSVWriter.tempTableForming;
import static com.anvizent.minidw.service.utils.helper.WebServiceJSONWriter.closeJsonfileWriter;
import static com.anvizent.minidw.service.utils.helper.WebServiceJSONWriter.writeObjectToJson;
import static com.anvizent.minidw.service.utils.helper.WebServiceXMLWriter.writeObjectToXml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.scribe.exceptions.OAuthSignatureException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.services.Base64Encoder;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.anvizent.minidw.service.utils.helper.ApiJsonFlatten;
import com.anvizent.minidw.service.utils.helper.WebServiceCSVWriter;
import com.anvizent.minidw.service.utils.helper.WebServiceJSONWriter;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.ClientWebserviceRequestException;
import com.datamodel.anvizent.errorhandler.MyResponseErrorHandler;
import com.datamodel.anvizent.helper.OAuthConstants;
import com.datamodel.anvizent.helper.WebServiceConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.OAuth1;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.fasterxml.jackson.databind.ObjectMapper;

import sun.misc.BASE64Encoder;

/**
 * 1 - No Auth , 2 - Authentication , 3 - Basic Auth , 4 - OAuth1 , 5 - OAuth2,
 * 6 - Session Based Auth
 * 
 * 
 * Modification date:- 26/11/2018 Modified By:-mahender.alaveni Modification For
 * :- Implemented Retry schedule.
 */

public class WebServiceUtils
{
	protected static final Log LOG = LogFactory.getLog(WebServiceUtils.class);
	static HttpHeaders sessionCookieHeaders = null;

	/**
	 * @param webServiceConnectionMaster
	 * @param restTemplate
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseEntity<Map> testAuthenticationUrl(WebServiceConnectionMaster webServiceConnectionMaster, RestTemplate restTemplate, Map<String, Object> clientDbDetails) throws Exception
	{
		String authenticationUrl = null, loginUrl = null;
		String authenticationUrlMethodType = null;
		ResponseEntity<Map> response = null;
		String authPathParams = "";
		JSONObject authPathParamJsonObj = null;
		String authRequestParams = "";
		JSONObject authRequestParamJsonObj = null;
		JSONObject sessionAuthUrlObj = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			headers.add("Accept", "application/json");

			Long authenticationType = webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId();
			/* for No Auth */
			if( authenticationType == WebServiceConstants.AuthenticationType.NO_AUTHENTICATION.type )
			{
				return new ResponseEntity<Map>(new HashMap<>(), HttpStatus.OK);
			}
			/* for basic and OAuth2 Authentication */
			if( StringUtils.isNotEmpty(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiSessionAuthURL()) )
			{
				sessionAuthUrlObj = new JSONObject(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiSessionAuthURL());
			}
			if( webServiceConnectionMaster.getWebServiceTemplateMaster().isBaseUrlRequired() )
			{
				if( authenticationType == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type && sessionAuthUrlObj.length() != 0 )
				{
					loginUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl() + sessionAuthUrlObj.getString("loginUrl");
				}
				else
				{
					authenticationUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl() + webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl();
				}
			}
			else
			{
				if( authenticationType == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type && sessionAuthUrlObj.length() != 0 )
				{
					loginUrl = sessionAuthUrlObj.getString("loginUrl");
				}
				else
				{
					authenticationUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl();
				}
			}
			authPathParams = webServiceConnectionMaster.getAuthPathParams();

			if( authPathParams != null )
			{
				authPathParamJsonObj = new JSONObject(authPathParams);
				Iterator<String> keys = authPathParamJsonObj.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					authenticationUrl = StringUtils.replace(authenticationUrl, "{#" + key + "}", authPathParamJsonObj.getString(key));
				}
			}

			authRequestParams = webServiceConnectionMaster.getRequestParams();
			StringBuilder requestParams = new StringBuilder();
			if( StringUtils.isNotBlank(authRequestParams) )
			{
				authRequestParamJsonObj = new JSONObject(authRequestParams);
				Iterator<String> keys = authRequestParamJsonObj.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					requestParams.append(key).append("=").append(authRequestParamJsonObj.getString(key)).append("&");
				}
			}
			JSONObject authBodyParamJsonObj = null;
			String authBodyParams = webServiceConnectionMaster.getBodyParams();
			JSONObject bodyParams = new JSONObject();
			if( StringUtils.isNotBlank(authBodyParams) )
			{
				authBodyParamJsonObj = new JSONObject(authBodyParams);
				Iterator<String> keys = authBodyParamJsonObj.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					bodyParams.put(key, authBodyParamJsonObj.getString(key));
				}
			}

			if( requestParams.length() > 0 )
			{
				requestParams.deleteCharAt(requestParams.length() - 1);
				authenticationUrl += (authenticationUrl.contains("?") ? "&" : "?") + requestParams;
			}

			authenticationUrlMethodType = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationMethodType();

			if( authenticationType == WebServiceConstants.AuthenticationType.AUTHENTICATION.type )
			{
				URI uri = UriComponentsBuilder.fromUriString(authenticationUrl).build().encode().toUri();
				if( authenticationUrlMethodType.equalsIgnoreCase(WebServiceConstants.HTTP_POST) )
				{
					try
					{
						HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(bodyParams.toString(), headers);
						response = restTemplate.postForEntity(uri, headerParamsPost, Map.class);
					}
					catch ( Exception exception )
					{
						response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
					}
				}
				else if( authenticationUrlMethodType.equals(WebServiceConstants.HTTP_GET) )
				{
					try
					{
						response = restTemplate.getForEntity(uri, Map.class);
					}
					catch ( Exception exception )
					{
						response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
					}
				}
			}
			else if( authenticationType == WebServiceConstants.AuthenticationType.BASIC_AUTHENTICATION.type )
			{
				Map<String, String> resp = new HashMap<>();
				if( authRequestParamJsonObj != null )
				{

					String authString = authRequestParamJsonObj.getString("Username") + ":" + authRequestParamJsonObj.getString("Password");
					String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
					resp.put(WebServiceConstants.AUTHORIZATION, "Basic " + authStringEnc);
					response = new ResponseEntity<Map>(resp, HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<Map>(resp, HttpStatus.BAD_REQUEST);
				}

			}
			else if( authenticationType == WebServiceConstants.AuthenticationType.OAUTH1.type )
			{
				Map<String, String> resp = new HashMap<String, String>();

				resp = getOAuth1AccessToken(webServiceConnectionMaster);

				return new ResponseEntity<Map>(resp, HttpStatus.OK);
			}
			else if( authenticationType == WebServiceConstants.AuthenticationType.OAUTH2.type )
			{
				Map<String, String> resp = new HashMap<>();

				if( webServiceConnectionMaster.getId() != null && webServiceConnectionMaster.getId() != 0 )
				{
					if( StringUtils.isNotBlank(webServiceConnectionMaster.getAuthenticationRefreshToken()) )
					{
						resp = getAccesTokenWithRefreshToken(webServiceConnectionMaster, clientDbDetails);
					}
					else
					{
						resp.put(WebServiceConstants.ACCESS_TOKEN, webServiceConnectionMaster.getAuthenticationToken());
					}
				}
				else
				{
					if( authenticationUrlMethodType.equalsIgnoreCase(WebServiceConstants.HTTP_POST) )
					{
						resp = getAccesToken(webServiceConnectionMaster, clientDbDetails);
					}
					else if( authenticationUrlMethodType.equals(WebServiceConstants.HTTP_GET) )
					{
						String tokenLocation = null;

						if( webServiceConnectionMaster.getWebServiceTemplateMaster().isBaseUrlRequired() )
						{
							tokenLocation = webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl() + webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
						}
						else
						{
							tokenLocation = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
						}
						String clientidentifier = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getClientIdentifier().trim();
						String clientScret = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getClientSecret().trim();
						String code = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getAuthCodeValue();
						String scope = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getScope();
						String state = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getState();

						if( StringUtils.isNotBlank(webServiceConnectionMaster.getAuthenticationToken()) )
						{
							resp.put(WebServiceConstants.ACCESS_TOKEN, webServiceConnectionMaster.getAuthenticationToken());
						}
						else
						{
							StringBuilder url = new StringBuilder();

							if( tokenLocation.contains("?") )
							{
								url.append(tokenLocation).append("&").append(OAuthConstants.CLIENT_ID).append("=").append(clientidentifier).append("&").append(OAuthConstants.CLIENT_SECRET).append("=").append(clientScret).append("&").append(OAuthConstants.CODE).append("=").append(code);
								if( StringUtils.isNotBlank(scope) )
								{
									url.append(OAuthConstants.SCOPE).append("=").append(scope);
								}
								if( StringUtils.isNotBlank(state) )
								{
									url.append(OAuthConstants.STATE).append("=").append(state);
								}
							}
							else
							{
								url.append(tokenLocation).append("?").append(OAuthConstants.CLIENT_ID).append("=").append(clientidentifier).append("&").append(OAuthConstants.CLIENT_SECRET).append("=").append(clientScret).append("&").append(OAuthConstants.CODE).append("=").append(code);
								if( StringUtils.isNotBlank(scope) )
								{
									url.append(OAuthConstants.SCOPE).append("=").append(scope);
								}
								if( StringUtils.isNotBlank(state) )
								{
									url.append(OAuthConstants.STATE).append("=").append(state);
								}
							}
							response = restTemplate.getForEntity(url.toString(), Map.class, headers);
							resp = response.getBody();
						}
					}
				}

				if( resp == null || resp.size() == 0 )
				{
					return new ResponseEntity<Map>(resp, HttpStatus.BAD_REQUEST);
				}

				return new ResponseEntity<Map>(resp, HttpStatus.OK);
			}
			else if( authenticationType == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type )
			{
				URI loginUri = UriComponentsBuilder.fromHttpUrl(loginUrl).build().encode().toUri();
				if( authenticationUrlMethodType.equalsIgnoreCase(WebServiceConstants.HTTP_POST) )
				{
					try
					{
						HttpEntity<Object> headerParamPost = new HttpEntity<Object>(bodyParams.toString(), headers);
						response = restTemplate.postForEntity(loginUri, headerParamPost, Map.class);
						setCookieHeaders(response);
					}
					catch ( Exception exception )
					{
						if( exception instanceof HttpServerErrorException )
						{
							HttpServerErrorException httpServerErrorException = (HttpServerErrorException) exception;
							if( httpServerErrorException.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR) )
							{
								response = new ResponseEntity<Map>(null, HttpStatus.INTERNAL_SERVER_ERROR);
							}
						}
					}
				}
				else if( authenticationUrlMethodType.equalsIgnoreCase(WebServiceConstants.HTTP_GET) )
				{
					try
					{
						response = restTemplate.getForEntity(loginUri, Map.class);
						setCookieHeaders(response);
					}
					catch ( Exception exception )
					{
						response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
					}
				}
			}
		}
		catch ( Exception e )
		{
			throw new Exception(e.getMessage());
		}

		return response;
	}

	/**
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	private static void setCookieHeaders(ResponseEntity<Map> response)
	{
		sessionCookieHeaders = new HttpHeaders();
		if( response != null )
		{
			sessionCookieHeaders.add("Content-Type", WebServiceConstants.JSON_CONTENT);
			sessionCookieHeaders.add("Accept", WebServiceConstants.JSON_CONTENT);

			Set<Entry<String, List<String>>> headersSet = response.getHeaders().entrySet();
			Iterator<Entry<String, List<String>>> iterator = headersSet.iterator();

			while (iterator.hasNext())
			{
				Entry<String, List<String>> key = iterator.next();
				if( key.getKey().equals("Set-Cookie") )
				{
					for (String cookie : key.getValue())
					{
						sessionCookieHeaders.add("Cookie", cookie);
					}
				}
			}
		}
	}

	/**
	 * @param webService
	 * @param clientDbDetails
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static Map<String, String> getAccesToken(WebServiceConnectionMaster webService, Map<String, Object> clientDbDetails)
	{
		String tokenLocation = null;
		String clientidentifier = null;
		String redirectUrl = null;
		String clientScret = null;
		HttpResponse httpResponse = null;
		String accessToken = null;
		String grantType = null;
		String refreshToken = null;
		String scope = null;
		String state = null;
		OAuth2 oauth2 = new OAuth2();
		Map<String, String> map = new HashMap<String, String>();
		HttpClient client = null;
		try
		{

			if( webService != null )
			{
				client = HttpClientBuilder.create().build();
				if( webService.getWebServiceTemplateMaster().isBaseUrlRequired() )
				{
					tokenLocation = webService.getWebServiceTemplateMaster().getBaseUrl() + webService.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
				}
				else
				{
					tokenLocation = webService.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
				}
				clientidentifier = webService.getWebServiceTemplateMaster().getoAuth2().getClientIdentifier().trim();
				redirectUrl = webService.getWebServiceTemplateMaster().getoAuth2().getRedirectUrl().trim();
				clientScret = webService.getWebServiceTemplateMaster().getoAuth2().getClientSecret().trim();
				grantType = webService.getWebServiceTemplateMaster().getoAuth2().getGrantType().trim();
				scope = webService.getWebServiceTemplateMaster().getoAuth2().getScope().trim();
				state = webService.getWebServiceTemplateMaster().getoAuth2().getState().trim();

				HttpPost post = new HttpPost(tokenLocation);

				List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

				parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE, grantType));

				parametersBody.add(new BasicNameValuePair(OAuthConstants.CODE, webService.getWebServiceTemplateMaster().getoAuth2().getAuthCodeValue()));

				parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID, clientidentifier));

				parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET, clientScret));

				parametersBody.add(new BasicNameValuePair(OAuthConstants.REDIRECT_URI, redirectUrl));

				if( StringUtils.isNotBlank(scope) )
				{
					parametersBody.add(new BasicNameValuePair(OAuthConstants.SCOPE, scope));
				}
				if( StringUtils.isNotBlank(state) )
				{
					parametersBody.add(new BasicNameValuePair(OAuthConstants.STATE, state));
				}
				post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

				httpResponse = client.execute(post);

				int statusCode = httpResponse.getStatusLine().getStatusCode();

				map = handleResponse(httpResponse);

				if( statusCode == WebServiceConstants.HTTP_STATUS_OK )
				{
					accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
					refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);
					oauth2.setAccessTokenValue(accessToken);
					oauth2.setRefreshTokenValue(refreshToken);
				}

			}

		}
		catch ( UnsupportedEncodingException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	public static void main(String[] args)
	{
		WebServiceConnectionMaster webService = new WebServiceConnectionMaster();
		WebServiceTemplateMaster webServiceTemplateMaster = new WebServiceTemplateMaster();
		OAuth2 oAuth2 = new OAuth2();
		webService.setWebServiceTemplateMaster(webServiceTemplateMaster);
		webServiceTemplateMaster.setBaseUrlRequired(false);
		webServiceTemplateMaster.setoAuth2(oAuth2);
		oAuth2.setAccessTokenUrl("https://accounts.google.com/o/oauth2/token");
		oAuth2.setClientIdentifier("1008728103824-u7ldbehfe9ls12bej6u5ktcj1qebd4k2.apps.googleusercontent.com");
		oAuth2.setClientSecret("u3TK-wPSS_aBL1SI2160wFfK");
		oAuth2.setRedirectUrl("https://schedulerv3.anvizent.com/minidw/adt/package/webServiceConnection/webServiceOAuth2Authenticationcallback");
		oAuth2.setGrantType("authorization_code");
		oAuth2.setAuthCodeValue("4/AABwtmxeMUH2gIvabslGmpUAMXDezAmzJwZ1o5BJuMNqE93SLI-V5qHYD40zLSjcnXkgYua0wuI-pGSQS-B5bZE");
		System.out.println(getAccesToken(webService, null));

	}

	/**
	 * @param webService
	 * @param clientDbDetails
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static Map<String, String> getAccesTokenWithRefreshToken(WebServiceConnectionMaster webService, Map<String, Object> clientDbDetails)
	{
		String tokenLocation = null;
		String clientidentifier = null;
		String clientScret = null;
		HttpResponse httpResponse = null;
		String accessToken = null;
		String refreshToken = null;
		OAuth2 oauth2 = new OAuth2();
		Map<String, String> map = new HashMap<String, String>();
		HttpClient client = null;
		try
		{
			if( webService != null )
			{

				client = HttpClientBuilder.create().build();

				if( webService.getWebServiceTemplateMaster().isBaseUrlRequired() )
				{
					tokenLocation = webService.getWebServiceTemplateMaster().getBaseUrl() + webService.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
				}
				else
				{
					tokenLocation = webService.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl().trim();
				}

				clientidentifier = webService.getWebServiceTemplateMaster().getoAuth2().getClientIdentifier().trim();
				clientScret = webService.getWebServiceTemplateMaster().getoAuth2().getClientSecret().trim();
				refreshToken = webService.getAuthenticationRefreshToken();

				if( !isValid(refreshToken) )
				{
					throw new RuntimeException("Please provide valid refresh token.");
				}
				if( !isValid(tokenLocation) )
				{
					throw new RuntimeException("Please provide valid token location.");
				}

				HttpPost post = new HttpPost(tokenLocation);

				List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

				parametersBody.add(new BasicNameValuePair(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN));

				parametersBody.add(new BasicNameValuePair(OAuthConstants.REFRESH_TOKEN, refreshToken));

				if( isValid(clientidentifier) )
				{
					parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_ID, clientidentifier));
				}

				if( isValid(clientScret) )
				{
					parametersBody.add(new BasicNameValuePair(OAuthConstants.CLIENT_SECRET, clientScret));
				}

				post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
				httpResponse = client.execute(post);
				int code = httpResponse.getStatusLine().getStatusCode();
				map = handleResponse(httpResponse);
				if( code == WebServiceConstants.HTTP_STATUS_OK )
				{
					accessToken = map.get(OAuthConstants.ACCESS_TOKEN);
					refreshToken = map.get(OAuthConstants.REFRESH_TOKEN);
					oauth2.setAccessTokenValue(accessToken);
					oauth2.setRefreshTokenValue(refreshToken);
					updateWebserviceTokens(webService.getId(), clientDbDetails, map);
				}
			}
		}
		catch ( ClientProtocolException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	/**
	 * @param webserviceConnectionId
	 * @param clientDbDetails
	 * @param tokens
	 */
	static void updateWebserviceTokens(Long webserviceConnectionId, Map<String, Object> clientDbDetails, Map<String, String> tokens)
	{
		Connection connection = null;
		try
		{
			connection = getDBConnection(clientDbDetails, false, true);
			int result = WebServiceCSVWriter.updateTokens(webserviceConnectionId, connection, tokens);
			LOG.info("Webservice Access Token & Refresh Token update status ::::: " + result);
		}
		catch ( ClassNotFoundException | SQLException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			if( connection != null )
			{
				closeConnection(connection);
			}
		}
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isValid(String str)
	{
		return (str != null && str.trim().length() > 0);
	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map handleResponse(HttpResponse response)
	{
		String contentType = OAuthConstants.JSON_CONTENT;
		if( response.getEntity().getContentType() != null )
		{
			contentType = response.getEntity().getContentType().getValue();
		}
		if( contentType.contains(OAuthConstants.JSON_CONTENT) )
		{
			return handleJsonResponse(response);
		}
		else if( contentType.contains(OAuthConstants.XML_CONTENT) || contentType.contains(OAuthConstants.TEXT_XML_CONTENT) )
		{
			return handleXMLResponse(response);
		}
		else
		{
			throw new RuntimeException("Cannot handle " + contentType + " content type. Supported content types include JSON, XML and URLEncoded");
		}

	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map handleJsonResponse(HttpResponse response)
	{
		Map<String, String> oauthLoginResponse = null;

		try
		{
			oauthLoginResponse = (Map<String, String>) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e.getMessage());
		}
		LOG.debug("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet())
		{
			LOG.debug(String.format("  %s = %s", entry.getKey(), entry.getValue()));
		}
		return oauthLoginResponse;
	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map handleXMLResponse(HttpResponse response)
	{
		Map<String, String> oauthResponse = new HashMap<String, String>();
		try
		{

			String xmlString = EntityUtils.toString(response.getEntity());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);

			LOG.debug("********** Response Receieved **********");
			parseXMLDoc(null, doc, oauthResponse);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RuntimeException("Exception occurred while parsing XML response");
		}
		return oauthResponse;
	}

	/**
	 * @param element
	 * @param doc
	 * @param oauthResponse
	 */
	public static void parseXMLDoc(Element element, Document doc, Map<String, String> oauthResponse)
	{
		NodeList child = null;
		if( element == null )
		{
			child = doc.getChildNodes();

		}
		else
		{
			child = element.getChildNodes();
		}
		for (int j = 0; j < child.getLength(); j++)
		{
			if( child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE )
			{
				org.w3c.dom.Element childElement = (org.w3c.dom.Element) child.item(j);
				if( childElement.hasChildNodes() )
				{
					LOG.debug(childElement.getTagName() + " : " + childElement.getTextContent());
					oauthResponse.put(childElement.getTagName(), childElement.getTextContent());
					parseXMLDoc(childElement, null, oauthResponse);
				}

			}
		}
	}

	/**
	 * @param webServiceApi
	 * @param restTemplate
	 * @param clientDbDetails
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static List<LinkedHashMap<String, Object>> validateWebService(WebServiceApi webServiceApi, RestTemplate restTemplate, Map<String, Object> clientDbDetails) throws JSONException, Exception
	{
		return validateWebService(webServiceApi, restTemplate, null, clientDbDetails);
	}

	/**
	 * @param webServiceApi
	 * @param restTemplate
	 * @param filePath
	 * @param clientDbDetails
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<LinkedHashMap<String, Object>> validateWebService(WebServiceApi webServiceApi, RestTemplate restTemplate, String filePath, Map<String, Object> clientDbDetails) throws JSONException, Exception
	{
		ResponseEntity authenticationResponse = null;

		List<LinkedHashMap<String, Object>> formattedApiResponse = null;
		Map<String, List<Object>> pathParamSubUrlDetails = new HashMap<>();

		if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebserviceType().endsWith(WebServiceConstants.WebserviceType.SOAP.type) )
		{
			authenticationResponse = testAuthenticationUrl(restTemplate, webServiceApi.getWebServiceConnectionMaster());
		}
		else
		{
			authenticationResponse = testAuthenticationUrl(webServiceApi.getWebServiceConnectionMaster(), restTemplate, clientDbDetails);
		}
		StringJoiner requiredRequestParamsPairs = new StringJoiner("&");
		String requiredRequestHeaderValue = "";
		if( authenticationResponse.getStatusCode().equals(HttpStatus.OK) || authenticationResponse.getStatusCode().equals(HttpStatus.NO_CONTENT) )
		{
			Map autheticateParams = null;
			if( authenticationResponse.getBody() != null && authenticationResponse.getBody() instanceof Map )
			{
				autheticateParams = (Map) authenticationResponse.getBody();
			}
			Long authenticationType = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId();
			String timeZone = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getTimeZone();
			String dateFormat = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getDateFormat();
			HttpHeaders headers = new HttpHeaders();
			if( authenticationType != WebServiceConstants.AuthenticationType.NO_AUTHENTICATION.type )
			{
				headers.add("Content-Type", "application/json");
				headers.add("Accept", "application/json");
			}
			Set<Entry<String, List<String>>> headersSet = authenticationResponse.getHeaders().entrySet();
			Iterator<Entry<String, List<String>>> iterator = headersSet.iterator();

			while (iterator.hasNext())
			{

				Entry<String, List<String>> key = iterator.next();

				if( key.getKey().equals("Set-Cookie") )
				{
					for (String cookie : key.getValue())
					{
						headers.add("Cookie", cookie);
					}
				}

			}
			/* add api authentication body parameters here */
			JSONObject postBodyObject = new JSONObject();
			if( authenticationType != WebServiceConstants.AuthenticationType.NO_AUTHENTICATION.type )
			{
				String requiredRequestParams = "";
				requiredRequestParams = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthRequestParams();
				if( StringUtils.isNotBlank(requiredRequestParams) && autheticateParams != null )
				{
					JSONObject requiredRequestParamsJObj = new JSONObject(requiredRequestParams);
					Iterator<String> requiredRequestParamsJObjInterator = (Iterator<String>) requiredRequestParamsJObj.keySet().iterator();
					while (requiredRequestParamsJObjInterator.hasNext())
					{
						String paramKey = requiredRequestParamsJObjInterator.next();
						String paramValue = requiredRequestParamsJObj.getString(paramKey);
						ArrayList<String> autheticateParamVariables = getParameterVariables(paramValue, "{$");

						for (String variable : autheticateParamVariables)
						{
							if( autheticateParams.get(variable) != null )
							{
								paramValue = StringUtils.replace(paramValue, "{$" + variable + "}", autheticateParams.get(variable).toString());
							}
							else
							{

								throw new ClientWebserviceRequestException("required parameters not found in authentication response");
							}
						}
						requiredRequestParamsPairs.add(paramKey + "=" + paramValue);
					}
				}
				String requiredBodyParams = "";
				requiredBodyParams = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthBodyParams();
				if( StringUtils.isNotBlank(requiredBodyParams) && requiredBodyParams != null && autheticateParams != null )
				{
					JSONObject requiredBodyParamsJObj = new JSONObject(requiredBodyParams);
					Iterator<String> requiredBodyParamsJObjInterator = (Iterator<String>) requiredBodyParamsJObj.keySet().iterator();
					while (requiredBodyParamsJObjInterator.hasNext())
					{
						String paramKey = requiredBodyParamsJObjInterator.next();
						String paramValue = requiredBodyParamsJObj.getString(paramKey);
						ArrayList<String> autheticateBodyParamVariables = getParameterVariables(paramValue, "{$");

						for (String variable : autheticateBodyParamVariables)
						{
							if( autheticateParams.get(variable) != null )
							{
								paramValue = StringUtils.replace(paramValue, "{$" + variable + "}", autheticateParams.get(variable).toString());
							}
							else
							{
								throw new ClientWebserviceRequestException("required parameters not found in authentication response");
							}
						}
						postBodyObject.put(paramKey, paramValue);
					}
				}

				String requiredRequestHeaders = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthRequestHeaders();

				if( requiredRequestHeaders != null && autheticateParams != null )
				{

					ArrayList<String> autheticateParamVariables = getParameterVariables(requiredRequestHeaders, "{$");
					for (String variable : autheticateParamVariables)
					{
						if( autheticateParams.get(variable) != null )
						{
							requiredRequestHeaders = StringUtils.replace(requiredRequestHeaders, "{$" + variable + "}", autheticateParams.get(variable).toString());
						}
						else
						{
							throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
						}
					}

					ArrayList<String> autheticateHeaderVariables = getParameterVariables(requiredRequestHeaders, "{#");

					if( autheticateHeaderVariables.size() > 0 )
					{
						String headerKeyvalues = webServiceApi.getWebServiceConnectionMaster().getHeaderKeyvalues();
						if( headerKeyvalues != null )
						{
							JSONObject headerKeyvaluesObj = new JSONObject(headerKeyvalues);
							for (String variable : autheticateHeaderVariables)
							{
								if( headerKeyvaluesObj.get(variable) != null )
								{
									requiredRequestHeaders = StringUtils.replace(requiredRequestHeaders, "{#" + variable + "}", headerKeyvaluesObj.get(variable).toString());
								}
								else
								{
									throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
								}
							}
						}
						else
						{
							throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
						}
					}

					requiredRequestHeaderValue = requiredRequestHeaders;

					String[] rHeaders = StringUtils.split(requiredRequestHeaderValue, ";");

					for (String hdr : rHeaders)
					{
						String[] eachHeader = StringUtils.split(hdr, ":");
						headers.add(eachHeader[0], eachHeader[1]);
					}

				}

				if( authenticationType == WebServiceConstants.AuthenticationType.BASIC_AUTHENTICATION.type )
				{
					if( autheticateParams.get(WebServiceConstants.AUTHORIZATION) != null )
					{
						headers.add(WebServiceConstants.AUTHORIZATION, autheticateParams.get(WebServiceConstants.AUTHORIZATION).toString());
					}
					else
					{
						throw new ClientWebserviceRequestException("Basic authentication failed");
					}

				}

				String pathParams = "";
				pathParams = webServiceApi.getApiPathParams();
				if( StringUtils.isNotBlank(pathParams) )
				{
					JSONObject pathParamsJObj = new JSONObject(pathParams);
					String apiUrl = webServiceApi.getApiUrl();
					ArrayList<String> pathParamVariables = getParameterVariables(apiUrl, "{#");
					for (String paramKey : pathParamVariables)
					{
						JSONObject paramValueObj = pathParamsJObj.getJSONObject(paramKey);

						if( paramValueObj != null )
						{
							if( paramValueObj.get("valueType").equals("M") )
							{
								apiUrl = StringUtils.replace(apiUrl, "{#" + paramKey + "}", paramValueObj.get("manualParamValue").toString());
							}
							else if( paramValueObj.get("valueType").equals("S") )
							{
								String subUrldetailsurl = null;
								boolean baseUrlRequired = (boolean) paramValueObj.get("baseUrlRequired");
								if( baseUrlRequired )
								{
									subUrldetailsurl = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getBaseUrl() + paramValueObj.get("subUrldetailsurl").toString();
								}
								else
								{
									subUrldetailsurl = paramValueObj.get("subUrldetailsurl").toString();
								}
								if( requiredRequestParamsPairs.toString().trim().length() > 0 )
								{
									subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + requiredRequestParamsPairs;
								}
								String subUrldetailsmethodType = paramValueObj.get("subUrldetailsmethodType").toString();
								String subUrldetailsresponseObjName = paramValueObj.get("subUrldetailsresponseObjName").toString();
								boolean subUrlPaginationRequired = (boolean) paramValueObj.get("subUrlPaginationRequired");

								boolean subUrlIncrementalUpdate = getJSONObjectKeyAsBoolean(paramValueObj, "subUrlIncrementalUpdate");
								if( subUrlIncrementalUpdate )
								{
									String subUrlIncrementalUpdateParamName = getJSONObjectKey(paramValueObj, "subUrlIncrementalUpdateParamName");
									String subUrlIncrementalUpdateParamvalue = getJSONObjectKey(paramValueObj, "subUrlIncrementalUpdateParamvalue");
									String subUrlIncrementalUpdateParamType = getJSONObjectKey(paramValueObj, "subUrlIncrementalUpdateParamType");
									String subUrlIncrementalDate = webServiceApi.getInclUpdateDate();
									if( webServiceApi.getValidateOrPreview() )
									{
										subUrlIncrementalDate = WebServiceConstants.INCREMENTAL_DATE;
									}
									if( StringUtils.isNotBlank(subUrlIncrementalDate) )
									{
										if( StringUtils.isNotBlank(timeZone) )
										{
											SimpleDateFormat sdf = new SimpleDateFormat(WebServiceConstants.INCREMENTAL_DATE_FORMAT);
											Date inclDateObj = null;
											try
											{
												inclDateObj = sdf.parse(subUrlIncrementalDate);
											}
											catch ( ParseException e )
											{
												LOG.error("Unable to parse date " + subUrlIncrementalDate + " for webservice incremental update ");
											}
											if( dateFormat.equals(WebServiceConstants.EPOCH_DATE_FORMAT) )
											{
												subUrlIncrementalDate = inclDateObj.getTime() + "";
											}
											else
											{
												SimpleDateFormat clientDateFormat = new SimpleDateFormat(dateFormat);
												clientDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
												subUrlIncrementalDate = clientDateFormat.format(inclDateObj);
											}
										}
									}
									if( StringUtils.isNotBlank(subUrlIncrementalUpdateParamName) )
									{
										if( StringUtils.contains(subUrlIncrementalUpdateParamvalue, "/*") && StringUtils.contains(subUrlIncrementalUpdateParamvalue, "*/") )
										{
											if( StringUtils.isNotBlank(subUrlIncrementalDate) )
											{
												if( StringUtils.contains(subUrlIncrementalUpdateParamvalue, "{date}") )
												{
													subUrlIncrementalUpdateParamvalue = StringUtils.replace(subUrlIncrementalUpdateParamvalue, "{date}", subUrlIncrementalDate);

													int startingIndex = StringUtils.indexOf(subUrlIncrementalUpdateParamvalue, "/*");
													int endingIndex = StringUtils.indexOf(subUrlIncrementalUpdateParamvalue, "*/", startingIndex);

													String newParamValue = "";
													newParamValue += StringUtils.substring(subUrlIncrementalUpdateParamvalue, 0, startingIndex);
													newParamValue += StringUtils.substring(subUrlIncrementalUpdateParamvalue, startingIndex + 2, endingIndex);
													newParamValue += StringUtils.substring(subUrlIncrementalUpdateParamvalue, endingIndex + 2, subUrlIncrementalUpdateParamvalue.length());
													subUrlIncrementalUpdateParamvalue = newParamValue;
												}
												else
												{
													throw new ClientWebserviceRequestException("Param value does not have date placeholder");
												}
											}
											else
											{
												int startingIndex = StringUtils.indexOf(subUrlIncrementalUpdateParamvalue, "/*");
												int endingIndex = StringUtils.indexOf(subUrlIncrementalUpdateParamvalue, "*/", startingIndex);
												String newParamValue = "";
												newParamValue += StringUtils.substring(subUrlIncrementalUpdateParamvalue, 0, startingIndex);
												newParamValue += StringUtils.substring(subUrlIncrementalUpdateParamvalue, endingIndex + 2, subUrlIncrementalUpdateParamvalue.length());
												subUrlIncrementalUpdateParamvalue = newParamValue;
											}
										}
										else
										{
											throw new ClientWebserviceRequestException(" Incremental update /*{date}*/ placeholder was not found");
										}

										if( StringUtils.isNotBlank(subUrlIncrementalDate) )
										{
											if( subUrlIncrementalUpdateParamType.equalsIgnoreCase(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
											{
												subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + subUrlIncrementalUpdateParamName + "=" + subUrlIncrementalUpdateParamvalue;
											}
											else
											{
												postBodyObject.put(subUrlIncrementalUpdateParamName, subUrlIncrementalUpdateParamvalue);
											}
										}
									}
								}

								List<Object> dataList = new ArrayList<>();
								if( subUrlPaginationRequired )
								{
									String subUrlPaginationType = paramValueObj.get("subUrlPaginationType").toString();
									if( subUrlPaginationType.equals(WebServiceConstants.PaginationType.OFFSET.type) )
									{
										String paginationParamType = paramValueObj.getString("subUrlPaginationParamType");
										String offsetParamName = paramValueObj.getString("subUrlPaginationOffSetRequestParamName");
										String offsetParamValue = paramValueObj.getString("subUrlPaginationOffSetRequestParamValue");
										String limitParamName = paramValueObj.getString("subUrlPaginationLimitRequestParamName");
										String limitParamValue = paramValueObj.getString("subUrlPaginationLimitRequestParamValue");
										int limit = Integer.parseInt(limitParamValue);
										int offset = Integer.parseInt(offsetParamValue);
										/*
										 * Sub URL offset pagination for Request
										 * Parameter
										 */
										if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
										{
											subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + limitParamName + "=" + limit + "&" + offsetParamName + "=";
											boolean isResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												getResponseObject(webServiceApi, subUrldetailsurl + offset, subUrldetailsmethodType, subUrldetailsresponseObjName, new JSONObject(), dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
											}
											else
											{
												while (isResultSetCompleted)
												{
													isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl + offset, subUrldetailsmethodType, subUrldetailsresponseObjName, new JSONObject(), dataList, autheticateParams, null, headers, restTemplate, limit, clientDbDetails);
													offset += limit;
												}
											}
										}
										else
										{
											/*
											 * Sub URL offset pagination for
											 * Body Parameter
											 */
											postBodyObject.put(offsetParamName, offset);
											postBodyObject.put(limitParamName, limit);

											boolean isResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
											}
											else
											{
												while (isResultSetCompleted)
												{
													isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, clientDbDetails);
													offset += limit;
													postBodyObject.put(offsetParamName, offset);
													postBodyObject.put(limitParamName, limit);
												}
											}
										}
									}
									else if( subUrlPaginationType.equals(WebServiceConstants.PaginationType.PAGE.type) )
									{
										String paginationParamType = paramValueObj.getString("subUrlPaginationParamType");
										String pageNumberParamName = paramValueObj.getString("subUrlPaginationPageNumberRequestParamName");
										String pageNumberParamValue = paramValueObj.getString("subUrlPaginationPageNumberRequestParamValue");
										String pageSizeParamName = paramValueObj.getString("subUrlPaginationPageSizeRequestParamName");
										String pageSizeParamValue = paramValueObj.getString("subUrlPaginationPageSizeRequestParamValue");
										int pageNumber = Integer.parseInt(pageNumberParamValue);
										int pageSize = Integer.parseInt(pageSizeParamValue);
										/*
										 * Sub URL page number and size
										 * pagination for Request Parameter
										 */
										if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
										{
											subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + pageSizeParamName + "=" + pageSize + "&" + pageNumberParamName + "=";
											boolean isResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												getResponseObject(webServiceApi, subUrldetailsurl + pageNumber, subUrldetailsmethodType, subUrldetailsresponseObjName, new JSONObject(), dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
											}
											else
											{
												while (isResultSetCompleted)
												{
													isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl + pageNumber, subUrldetailsmethodType, subUrldetailsresponseObjName, new JSONObject(), dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													pageNumber = pageNumber + 1;
												}
											}
										}
										else
										{
											/*
											 * Sub URL age number and size
											 * pagination pagination for Body
											 * Parameter
											 */
											postBodyObject.put(pageNumberParamName, pageNumber);
											postBodyObject.put(pageSizeParamName, pageSize);

											boolean isResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
											}
											else
											{
												while (isResultSetCompleted)
												{
													isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													pageNumber = pageNumber + 1;
													postBodyObject.put(pageNumberParamName, pageNumber);
													postBodyObject.put(pageSizeParamName, pageSize);
												}
											}
										}
									}
									else if( subUrlPaginationType.equals(WebServiceConstants.PaginationType.DATE.type) )
									{
										/* Sub URL date pagination */
										String subUrlPaginationParamType = paramValueObj.getString("subUrlPaginationParamType");
										String subUrlPaginationStartDateParam = paramValueObj.getString("subUrlPaginationStartDateParam");
										String subUrlPaginationEndDateParam = paramValueObj.getString("subUrlPaginationEndDateParam");
										String subUrlPaginationStartDate = paramValueObj.getString("subUrlPaginationStartDate");
										int subUrlPaginationDateRange = Integer.parseInt(paramValueObj.getString("subUrlPaginationDateRange"));

										String subUrlDatePageNumberParamName = getJSONObjectKey(paramValueObj, "subUrlPaginationDatePageNumberRequestParamName");
										String subUrlDatePageNumberParamValue = getJSONObjectKey(paramValueObj, "subUrlPaginationDatePageNumberRequestParamValue");
										String subUrlDatePageSizeParamName = getJSONObjectKey(paramValueObj, "subUrlPaginationDatePageSizeRequestParamName");
										String subUrlDatePageSizeParamValue = getJSONObjectKey(paramValueObj, "subUrlPaginationDatePageSizeRequestParamValue");

										int subUrlDatePageNumber = 0;
										int subUrlDatePageSize = 0;
										if( StringUtils.isNotBlank(subUrlDatePageNumberParamName) && StringUtils.isNotBlank(subUrlDatePageSizeParamName) )
										{
											subUrlDatePageNumber = Integer.parseInt(subUrlDatePageNumberParamValue);
											subUrlDatePageSize = Integer.parseInt(subUrlDatePageSizeParamValue);
										}
										/*
										 * Sub URL date pagination for Request
										 * Parameter
										 */
										if( subUrlPaginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
										{
											String startDate = convertDateFormat(dateFormat, subUrlPaginationStartDate, timeZone);
											String paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);
											subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + subUrlPaginationStartDateParam + "=%s" + "&" + subUrlPaginationEndDateParam + "=%s";
											boolean isResultSetCompleted = true;
											boolean isDatePageNumerSizeResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												if( StringUtils.isNotBlank(subUrlDatePageNumberParamName) && StringUtils.isNotBlank(subUrlDatePageSizeParamName) )
												{
													/*
													 * Sub URL date pagination
													 * with page number and size
													 */
													subUrldetailsurl += "&" + subUrlDatePageSizeParamName + "=" + subUrlDatePageSize + "&" + subUrlDatePageNumberParamName + "=%s";

													while (isResultSetCompleted)
													{
														while (isDatePageNumerSizeResultSetCompleted)
														{
															isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate, subUrlDatePageNumber), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList,
																	autheticateParams, null, headers, restTemplate, clientDbDetails);
															subUrlDatePageNumber = subUrlDatePageNumber + 1;
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isDatePageNumerSizeResultSetCompleted = true;
															}
														}
														subUrlDatePageNumber = 0;
														isDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														isResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers,
																restTemplate, clientDbDetails);
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
											}
											else
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlDatePageNumberParamName) && StringUtils.isNotBlank(subUrlDatePageSizeParamName) )
												{
													/*
													 * Sub URL date Pagination
													 * with page number and size
													 */
													subUrldetailsurl += "&" + subUrlDatePageSizeParamName + "=" + subUrlDatePageSize + "&" + subUrlDatePageNumberParamName + "=%s";

													while (isResultSetCompleted)
													{
														while (isDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days -> " + " date Page Number -> " + subUrlDatePageNumber
																	+ " date Page Size -> " + subUrlDatePageSize);
															isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate, subUrlDatePageNumber), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList,
																	autheticateParams, null, headers, restTemplate, clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlDatePageNumber = subUrlDatePageNumber + 1;
														}
														subUrlDatePageNumber = 0;
														isDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers,
																restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}

										}
										else
										{
											/*
											 * Sub URL date pagination for Body
											 * Parameter
											 */
											String startDate = convertDateFormat(dateFormat, subUrlPaginationStartDate, timeZone);
											String paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);
											postBodyObject.put(subUrlPaginationStartDateParam, startDate);
											postBodyObject.put(subUrlPaginationEndDateParam, paginationEndDate);

											boolean isResultSetCompleted = true;
											boolean isDatePageNumerSizeResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlDatePageNumberParamName) && StringUtils.isNotBlank(subUrlDatePageSizeParamName) )
												{
													/*
													 * Sub URL date pagination
													 * with page number and size
													 */
													while (isResultSetCompleted)
													{
														postBodyObject.put(subUrlDatePageSizeParamName, subUrlDatePageSize);
														postBodyObject.put(subUrlDatePageNumberParamName, subUrlDatePageNumber);

														while (isDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days-> " + " date Page Number -> " + subUrlDatePageNumber
																	+ " date Page Size -> " + subUrlDatePageSize);
															isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlDatePageNumber = subUrlDatePageNumber + 1;
															postBodyObject.put(subUrlDatePageSizeParamName, subUrlDatePageSize);
															postBodyObject.put(subUrlDatePageNumberParamName, subUrlDatePageNumber);
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isDatePageNumerSizeResultSetCompleted = true;
															}
														}
														subUrlDatePageNumber = 0;
														isDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														postBodyObject.put(subUrlPaginationStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationEndDateParam, paginationEndDate);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														postBodyObject.put(subUrlPaginationStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationEndDateParam, paginationEndDate);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
											}
											else
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlDatePageNumberParamName) && StringUtils.isNotBlank(subUrlDatePageSizeParamName) )
												{
													/*
													 * Sub URL date pagination
													 * with page number and size
													 */
													while (isResultSetCompleted)
													{
														postBodyObject.put(subUrlDatePageSizeParamName, subUrlDatePageSize);
														postBodyObject.put(subUrlDatePageNumberParamName, subUrlDatePageNumber);

														while (isDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days-> " + " date Page Number -> " + subUrlDatePageNumber
																	+ " date Page Size -> " + subUrlDatePageSize);
															isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlDatePageNumber = subUrlDatePageNumber + 1;
															postBodyObject.put(subUrlDatePageSizeParamName, subUrlDatePageSize);
															postBodyObject.put(subUrlDatePageNumberParamName, subUrlDatePageNumber);
														}
														subUrlDatePageNumber = 0;
														isDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														/*
														 * updating body param's
														 * with updated dates.
														 * 
														 */
														postBodyObject.put(subUrlPaginationStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationEndDateParam, paginationEndDate);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationDateRange);

														/*
														 * updating body param's
														 * with updated dates.
														 * 
														 */
														postBodyObject.put(subUrlPaginationStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationEndDateParam, paginationEndDate);

														String toDayDate = getToDate();
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
									}
									else if( subUrlPaginationType.equals(WebServiceConstants.PaginationType.INCREMENTAL_DATE.type) )
									{
										/*
										 * Sub URL incremental date pagination
										 */
										String paginationParamType = paramValueObj.getString("subUrlPaginationParamType");
										String subUrlPaginationIncrementalStartDateParam = paramValueObj.getString("subUrlPaginationIncrementalStartDateParam");
										String subUrlPaginationIncrementalStartDate = paramValueObj.getString("subUrlPaginationIncrementalStartDate");
										String subUrlPaginationIncrementalEndDateParam = paramValueObj.getString("subUrlPaginationIncrementalEndDateParam");
										String subUrlPaginationIncrementalEndDate = paramValueObj.getString("subUrlPaginationIncrementalEndDate");

										String subUrlIncrementalDatepageNumberParamName = getJSONObjectKey(paramValueObj, "subUrlPaginationIncrementalDatePageNumberRequestParamName");
										String subUrlIncrementalDatepageNumberParamValue = getJSONObjectKey(paramValueObj, "subUrlPaginationIncrementalDatePageNumberRequestParamValue");
										String subUrlIncrementalDatepageSizeParamName = getJSONObjectKey(paramValueObj, "subUrlPaginationIncrementalDatePageSizeRequestParamName");
										String subUrlIncrementalDatepageSizeParamValue = getJSONObjectKey(paramValueObj, "subUrlPaginationIncrementalDatePageSizeRequestParamValue");

										int subUrlIncrementalDatePageNumber = 0;
										int subUrlIncrementalDatePageSize = 0;
										if( StringUtils.isNotBlank(subUrlIncrementalDatepageNumberParamName) && StringUtils.isNotBlank(subUrlIncrementalDatepageSizeParamName) )
										{
											subUrlIncrementalDatePageNumber = Integer.parseInt(subUrlIncrementalDatepageNumberParamValue);
											subUrlIncrementalDatePageSize = Integer.parseInt(subUrlIncrementalDatepageSizeParamValue);
										}
										if( StringUtils.isEmpty(subUrlPaginationIncrementalEndDate) )
										{
											subUrlPaginationIncrementalEndDate = getToDate();
										}
										int subUrlPaginationIncrementalDateRange = Integer.parseInt(paramValueObj.getString("subUrlPaginationIncrementalDateRange"));
										/*
										 * Sub URL incremental date pagination
										 * for Request Parameter
										 * 
										 */
										if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
										{
											String startDate = convertDateFormat(dateFormat, subUrlPaginationIncrementalStartDate, timeZone);
											String paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);
											subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + subUrlPaginationIncrementalStartDateParam + "=%s" + "&" + subUrlPaginationIncrementalEndDateParam + "=%s";
											boolean isResultSetCompleted = true;
											boolean isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												if( StringUtils.isNotBlank(subUrlIncrementalDatepageNumberParamName) && StringUtils.isNotBlank(subUrlIncrementalDatepageSizeParamName) )
												{
													/*
													 * Sub URL incremental date
													 * pagination with page
													 * number and size
													 */
													subUrldetailsurl += "&" + subUrlIncrementalDatepageSizeParamName + "=" + subUrlIncrementalDatePageSize + "&" + subUrlIncrementalDatepageNumberParamName + "=%s";
													while (isResultSetCompleted)
													{
														while (isSubUrlIncrementalDatePageNumerSizeResultSetCompleted)
														{
															isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate, subUrlIncrementalDatePageNumber), subUrldetailsmethodType, subUrldetailsresponseObjName,
																	postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
															subUrlIncrementalDatePageNumber = subUrlIncrementalDatePageNumber + 1;
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
															}
														}
														subUrlIncrementalDatePageNumber = 0;
														isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														isResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers,
																restTemplate, clientDbDetails);
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
											}
											else
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlIncrementalDatepageNumberParamName) && StringUtils.isNotBlank(subUrlIncrementalDatepageSizeParamName) )
												{
													/*
													 * Sub URL incremental date
													 * pagination with page
													 * number and size
													 */
													subUrldetailsurl += "&" + subUrlIncrementalDatepageSizeParamName + "=" + subUrlIncrementalDatePageSize + "&" + subUrlIncrementalDatepageNumberParamName + "=%s";
													while (isResultSetCompleted)
													{
														while (isSubUrlIncrementalDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days-> " + " date Page Number -> "
																	+ subUrlIncrementalDatePageNumber + " Incremental date Page Size -> " + subUrlIncrementalDatePageSize);
															isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate, subUrlIncrementalDatePageNumber), subUrldetailsmethodType, subUrldetailsresponseObjName,
																	postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlIncrementalDatePageNumber = subUrlIncrementalDatePageNumber + 1;
														}
														subUrlIncrementalDatePageNumber = 0;
														isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers,
																restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}

										}
										else
										{
											/*
											 * Sub URL incremental date
											 * pagination for Body Parameter
											 */
											String startDate = convertDateFormat(dateFormat, subUrlPaginationIncrementalStartDate, timeZone);
											String paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);
											postBodyObject.put(subUrlPaginationIncrementalStartDateParam, startDate);
											postBodyObject.put(subUrlPaginationIncrementalEndDateParam, paginationEndDate);

											boolean isResultSetCompleted = true;
											boolean isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
											if( webServiceApi.getValidateOrPreview() )
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlIncrementalDatepageNumberParamName) && StringUtils.isNotBlank(subUrlIncrementalDatepageSizeParamName) )
												{
													/*
													 * Sub URL incremental date
													 * pagination with page
													 * number and size
													 */
													while (isResultSetCompleted)
													{
														postBodyObject.put(subUrlIncrementalDatepageSizeParamName, subUrlIncrementalDatePageSize);
														postBodyObject.put(subUrlIncrementalDatepageNumberParamName, subUrlIncrementalDatePageNumber);

														while (isSubUrlIncrementalDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days-> " + " date Page Number -> "
																	+ subUrlIncrementalDatePageNumber + " date Page Size -> " + subUrlIncrementalDatePageSize);
															isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
																	clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlIncrementalDatePageNumber = subUrlIncrementalDatePageNumber + 1;
															postBodyObject.put(subUrlIncrementalDatepageSizeParamName, subUrlIncrementalDatePageSize);
															postBodyObject.put(subUrlIncrementalDatepageNumberParamName, subUrlIncrementalDatePageNumber);
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
															}
														}
														subUrlIncrementalDatePageNumber = 0;
														isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														postBodyObject.put(subUrlPaginationIncrementalStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationIncrementalEndDateParam, paginationEndDate);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														postBodyObject.put(subUrlPaginationIncrementalStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationIncrementalEndDateParam, paginationEndDate);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
											}
											else
											{
												UUID uuid = UUID.randomUUID();
												LOG.info("Retrieval started for the Url : " + subUrldetailsurl + " with id: " + uuid);
												if( StringUtils.isNotBlank(subUrlIncrementalDatepageNumberParamName) && StringUtils.isNotBlank(subUrlIncrementalDatepageSizeParamName) )
												{
													/*
													 * Sub URL incremental date
													 * pagination with page
													 * number and size
													 */
													while (isResultSetCompleted)
													{
														postBodyObject.put(subUrlIncrementalDatepageSizeParamName, subUrlIncrementalDatePageSize);
														postBodyObject.put(subUrlIncrementalDatepageNumberParamName, subUrlIncrementalDatePageNumber);

														while (isSubUrlIncrementalDatePageNumerSizeResultSetCompleted)
														{
															LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days-> " + " date Page Number -> "
																	+ subUrlIncrementalDatePageNumber + " date Page Size -> " + subUrlIncrementalDatePageSize);
															isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
																	clientDbDetails);
															LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
															subUrlIncrementalDatePageNumber = subUrlIncrementalDatePageNumber + 1;
															postBodyObject.put(subUrlIncrementalDatepageSizeParamName, subUrlIncrementalDatePageSize);
															postBodyObject.put(subUrlIncrementalDatepageNumberParamName, subUrlIncrementalDatePageNumber);
														}
														subUrlIncrementalDatePageNumber = 0;
														isSubUrlIncrementalDatePageNumerSizeResultSetCompleted = true;
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														/*
														 * updating body params
														 * with updated dates.
														 * 
														 */
														postBodyObject.put(subUrlPaginationIncrementalStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationIncrementalEndDateParam, paginationEndDate);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + subUrlPaginationIncrementalDateRange + " days");
														isResultSetCompleted = getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														startDate = paginationEndDate;
														paginationEndDate = getEndDateByInterval(startDate, dateFormat, subUrlPaginationIncrementalDateRange);

														/*
														 * updating body params
														 * with updated dates.
														 * 
														 */
														postBodyObject.put(subUrlPaginationIncrementalStartDateParam, startDate);
														postBodyObject.put(subUrlPaginationIncrementalEndDateParam, paginationEndDate);

														String toDayDate = subUrlPaginationIncrementalEndDate;
														if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
									}
									else if( subUrlPaginationType.equals(WebServiceConstants.PaginationType.CONDITIONAL_DATE.type) )
									{
										JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
										for (int i = 0; i < paramters.length(); i++)
										{
											JSONObject paramObject = paramters.getJSONObject(i);
											String filterName = paramObject.getString("subUrlPaginationFilterName");
											String fromDateParam = paramObject.getString("subUrlPaginationConditionalFromDateParam");
											String fromDateCondition = paramObject.getString("subUrlPaginationConditionalFromDateCondition");
											String fromDate = paramObject.getString("subUrlPaginationConditionalFromDate");
											String conditionalParam = paramObject.getString("subUrlPaginationConditionalParam");
											String toDateParam = paramObject.getString("subUrlPaginationConditionalToDateParam");
											String toDateCondition = paramObject.getString("subUrlPaginationConditionalToDateCondition");
											String toDate = paramObject.getString("subUrlPaginationConditionalToDate");
											if( StringUtils.isEmpty(toDate) )
											{
												toDate = getToDate();
											}
											String paginationParamType = paramObject.getString("subUrlPaginationParamType");
											int dayRange = Integer.parseInt(paramObject.getString("subUrlPaginationConditionalDayRange"));

											/*
											 * sub URL conditional date
											 * pagination for Request Parameter
											 */
											if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
											{
												String startDate = fromDate;
												String paginationEndDate = null;
												if( dayRange > 0 )
												{
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
												}
												else
												{
													paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
												}
												subUrldetailsurl += (subUrldetailsurl.contains("?") ? "&" : "?") + filterName + " = " + fromDateParam + " " + fromDateCondition + " %s " + conditionalParam + " " + toDateParam + " " + toDateCondition + " %s";

												boolean isResultSetCompleted = true;
												if( webServiceApi.getValidateOrPreview() )
												{
													while (isResultSetCompleted)
													{
														getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
																clientDbDetails);
														startDate = paginationEndDate;
														if( dayRange > 0 )
														{
															paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
														}
														else
														{
															paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
														}

														if( compareDates(startDate, convertDateFormat(dateFormat, toDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															if( dataList.size() > 0 )
															{
																break;
															}
															else
															{
																isResultSetCompleted = true;
															}
														}
													}
												}
												else
												{
													while (isResultSetCompleted)
													{
														isResultSetCompleted = getResponseObject(webServiceApi, String.format(subUrldetailsurl, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null,
																headers, restTemplate, clientDbDetails);
														startDate = paginationEndDate;
														if( dayRange > 0 )
														{
															paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
														}
														else
														{
															paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
														}

														if( compareDates(startDate, convertDateFormat(dateFormat, toDate, timeZone), dateFormat) )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
											else
											{
												/*
												 * conditional date pagination
												 * for Body Parameter
												 */
											}
										}
									}
									else
									{
										/* Sub URL Pagination HyperLink */
										boolean isResultSetCompleted = true;

										if( webServiceApi.getValidateOrPreview() )
										{
											getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										}
										else
										{
											String paginationNextParam = paramValueObj.getString("subUrlPaginationHyperLinkPattern");

											int paginationLimit = -1;
											if( StringUtils.isNotBlank(paramValueObj.getString("subUrlPaginationHypermediaPageLimit")) )
											{
												paginationLimit = Integer.parseInt(paramValueObj.getString("subUrlPaginationHypermediaPageLimit"));
											}

											List<Object> nextPathList = new ArrayList<>();

											String nextUrlPath = subUrldetailsurl;

											do
											{
												isResultSetCompleted = getResponseObject(webServiceApi, nextUrlPath, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, paginationLimit,
														paginationNextParam, nextPathList, clientDbDetails);

												if( nextPathList.size() > 0 && (paginationLimit == -1 || (paginationLimit != -1 && !isResultSetCompleted)) )
												{

													Object nextUrlPathObject = nextPathList.get(0);
													if( nextUrlPathObject != null && nextUrlPathObject instanceof String && StringUtils.isNotBlank(nextUrlPathObject.toString()) )
													{
														nextUrlPath = nextUrlPathObject.toString();
													}
													else
													{
														nextUrlPath = null;
													}

													nextPathList.clear();
												}
												else
												{
													nextUrlPath = null;
												}
											}
											while (nextUrlPath != null);
										}

									}
								}
								else
								{
									getResponseObject(webServiceApi, subUrldetailsurl, subUrldetailsmethodType, subUrldetailsresponseObjName, new JSONObject(), dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
								}
								if( dataList != null && dataList.size() > 0 )
								{
									for (Object obj : dataList)
									{
										if( obj instanceof List || dataList.get(0) instanceof Map )
										{
											throw new ClientWebserviceRequestException("Sub url Response should not be List or Map");
										}
									}

									pathParamSubUrlDetails.put(paramKey, dataList);
								}
							}
							else
							{
								throw new ClientWebserviceRequestException("sub url Response type not found");
							}
						}
						else
						{
							throw new ClientWebserviceRequestException("Path Variable value not found");
						}

					}
					webServiceApi.setApiUrl(apiUrl);
				}
			}

			String apiUrl = null;
			if( webServiceApi.getBaseUrlRequired() )
			{
				apiUrl = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getBaseUrl() + webServiceApi.getApiUrl();
			}
			else
			{
				apiUrl = webServiceApi.getApiUrl();
			}

			if( requiredRequestParamsPairs.toString().trim().length() > 0 )
			{
				apiUrl += (apiUrl.contains("?") ? "&" : "?") + requiredRequestParamsPairs;
			}
			/* add API request parameters here */
			if( StringUtils.isNotBlank(webServiceApi.getApiRequestParams()) && StringUtils.startsWith(webServiceApi.getApiRequestParams(), "[") )
			{
				JSONArray paramters = new JSONArray(webServiceApi.getApiRequestParams());
				for (int i = 0; i < paramters.length(); i++)
				{
					JSONObject paramObject = paramters.getJSONObject(i);
					if( !paramObject.isNull("paramName") )
					{
						String paramName = paramObject.getString("paramName");
						String paramValue = paramObject.getString("paramValue");

						apiUrl += (apiUrl.contains("?") ? "&" : "?") + paramName + "=" + paramValue;
					}
				}
			}
			/* add API body parameters here */
			if( StringUtils.isNotBlank(webServiceApi.getApiBodyParams()) && StringUtils.startsWith(webServiceApi.getApiBodyParams(), "[") )
			{
				JSONArray paramters = new JSONArray(webServiceApi.getApiBodyParams());
				for (int i = 0; i < paramters.length(); i++)
				{
					JSONObject paramObject = paramters.getJSONObject(i);
					if( !paramObject.isNull("paramName") )
					{
						String paramName = paramObject.getString("paramName");
						String paramValue = paramObject.getString("paramValue");
						postBodyObject.put(paramName, paramValue);
					}
				}
			}
			/* incremental update */
			if( webServiceApi.getIncrementalUpdate() && StringUtils.isNotBlank(webServiceApi.getIncrementalUpdateparamdata()) && StringUtils.startsWith(webServiceApi.getIncrementalUpdateparamdata(), "[") )
			{
				String incrementalDate = webServiceApi.getInclUpdateDate();

				if( webServiceApi.getValidateOrPreview() )
				{
					incrementalDate = WebServiceConstants.INCREMENTAL_DATE;
				}
				if( StringUtils.isNotBlank(incrementalDate) )
				{
					if( StringUtils.isNotBlank(timeZone) )
					{
						SimpleDateFormat sdf = new SimpleDateFormat(WebServiceConstants.INCREMENTAL_DATE_FORMAT);
						Date inclDateObj = null;
						try
						{
							inclDateObj = sdf.parse(incrementalDate);
						}
						catch ( ParseException e )
						{
							LOG.error("Unable to parse date " + incrementalDate + " for webservice incremental update ");
						}
						if( dateFormat.equals(WebServiceConstants.EPOCH_DATE_FORMAT) )
						{
							incrementalDate = inclDateObj.getTime() + "";
						}
						else
						{
							SimpleDateFormat clientDateFormat = new SimpleDateFormat(dateFormat);
							clientDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
							incrementalDate = clientDateFormat.format(inclDateObj);
						}
					}
				}

				JSONArray paramters = new JSONArray(webServiceApi.getIncrementalUpdateparamdata());

				for (int i = 0; i < paramters.length(); i++)
				{
					JSONObject paramObject = paramters.getJSONObject(i);
					if( !paramObject.isNull("incrementalUpdateParamName") )
					{
						String paramName = paramObject.getString("incrementalUpdateParamName");
						String paramValue = paramObject.getString("incrementalUpdateParamvalue");
						String paramType = paramObject.getString("incrementalUpdateParamType");

						if( StringUtils.contains(paramValue, "/*") && StringUtils.contains(paramValue, "*/") )
						{
							if( StringUtils.isNotBlank(incrementalDate) )
							{
								if( StringUtils.contains(paramValue, "{date}") )
								{
									paramValue = StringUtils.replace(paramValue, "{date}", incrementalDate);

									int startingIndex = StringUtils.indexOf(paramValue, "/*");
									int endingIndex = StringUtils.indexOf(paramValue, "*/", startingIndex);

									String newParamValue = "";
									newParamValue += StringUtils.substring(paramValue, 0, startingIndex);
									newParamValue += StringUtils.substring(paramValue, startingIndex + 2, endingIndex);
									newParamValue += StringUtils.substring(paramValue, endingIndex + 2, paramValue.length());
									paramValue = newParamValue;
								}
								else
								{
									throw new ClientWebserviceRequestException("Param value does not have date placeholder");
								}
							}
							else
							{
								int startingIndex = StringUtils.indexOf(paramValue, "/*");
								int endingIndex = StringUtils.indexOf(paramValue, "*/", startingIndex);
								String newParamValue = "";
								newParamValue += StringUtils.substring(paramValue, 0, startingIndex);
								newParamValue += StringUtils.substring(paramValue, endingIndex + 2, paramValue.length());
								paramValue = newParamValue;
							}
						}
						else
						{
							throw new ClientWebserviceRequestException(" Incremental update /*{date}*/ placeholder was not found");
						}

						if( StringUtils.isNotBlank(incrementalDate) )
						{
							if( paramType.equalsIgnoreCase(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
							{
								apiUrl += (apiUrl.contains("?") ? "&" : "?") + paramName + "=" + paramValue;
							}
							else
							{
								postBodyObject.put(paramName, paramValue);
							}
						}
					}
				}
			}
			List<Object> dataList = null;
			List<String> apiUrls = new ArrayList<>();
			ArrayList<String> pathParamVariables = getParameterVariables(apiUrl, "{#");

			if( pathParamVariables.size() > 0 )
			{
				if( pathParamVariables.size() == 1 )
				{
					if( pathParamSubUrlDetails.size() == 1 )
					{
						String key = pathParamVariables.get(0);
						List<Object> values = pathParamSubUrlDetails.get(key);

						for (Object val : values)
						{
							String valWithType = "";
							if( val instanceof Integer )
							{
								valWithType = (Integer) val + "";
							}
							else if( val instanceof String )
							{
								valWithType = (String) val;
							}
							else if( val instanceof Long )
							{
								valWithType = (Long) val + "";
							}
							apiUrls.add(StringUtils.replace(apiUrl, "{#" + key + "}", valWithType));
						}
					}
					else
					{
						throw new ClientWebserviceRequestException("Sub url response is empty");
					}
				}
				else
				{
					throw new ClientWebserviceRequestException("Only one Sub url per Api is allowed");
				}
			}
			else
			{
				apiUrls.add(apiUrl);
			}
			dataList = new ArrayList<>();
			try
			{
				for (String url : apiUrls)
				{
					/* soap web service calling... */
					if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebserviceType().equals(WebServiceConstants.WebserviceType.SOAP.type) )
					{
						String soapBodyElement = webServiceApi.getSoapBodyElement();
						String soapBodyParams = webServiceApi.getWebServiceConnectionMaster().getBodyParams();
						if( soapBodyParams != null )
						{
							JSONObject soapBodyParamsJsonObj = new JSONObject(soapBodyParams);
							Iterator<String> keys = soapBodyParamsJsonObj.keys();
							while (keys.hasNext())
							{
								String key = keys.next();
								soapBodyElement = StringUtils.replace(soapBodyElement, "{$" + key + "}", soapBodyParamsJsonObj.getString(key));
							}
						}
						webServiceApi.setSoapBodyElement(soapBodyElement);
						headers = new HttpHeaders();
						headers.add("Content-Type", "text/plain");
						headers.add("SOAPAction", "search");

						if( webServiceApi.getPaginationRequired() && StringUtils.isNotBlank(webServiceApi.getPaginationRequestParamsData()) && StringUtils.startsWith(webServiceApi.getPaginationRequestParamsData(), "[") )
						{

							String searchId = null;
							JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
							for (int i = 0; i < paramters.length(); i++)
							{
								JSONObject paramObject = paramters.getJSONObject(i);
								String offsetParamName = paramObject.getString("paginationOffSetRequestParamName");
								String offsetParamValue = paramObject.getString("paginationOffSetRequestParamValue");
								String limitParamName = paramObject.getString("paginationLimitRequestParamName");
								String limitParamValue = paramObject.getString("paginationLimitRequestParamValue");
								String paginationParamType = paramObject.getString("paginationParamType");
								String paginationObjectName = paramObject.getString("paginationObjectName");
								String paginationSearchId = paramObject.getString("paginationSearchId");
								String paginationSoapBody = paramObject.getString("PaginationSoapBody");

								int limit = Integer.parseInt(limitParamValue);
								int offset = Integer.parseInt(offsetParamValue);

								if( paginationSoapBody != null )
								{
									JSONObject soapBodyParamsJsonObj = new JSONObject(soapBodyParams);
									Iterator<String> keys = soapBodyParamsJsonObj.keys();
									while (keys.hasNext())
									{
										String key = keys.next();
										paginationSoapBody = StringUtils.replace(paginationSoapBody, "{$" + key + "}", soapBodyParamsJsonObj.getString(key));
									}
								}

								if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
								{
									url += (url.contains("?") ? "&" : "?") + limitParamName + "=" + limit + "&" + offsetParamName + "=";
									boolean isResultSetCompleted = true;

									if( webServiceApi.getValidateOrPreview() )
									{
										getResponseObject(webServiceApi, url + offset, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
									}
									else
									{
										while (isResultSetCompleted)
										{
											isResultSetCompleted = getResponseObject(webServiceApi, url + offset, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, clientDbDetails);
										}
									}
								}
								else
								{
									// body parameter
									postBodyObject.put(offsetParamName, offset);
									postBodyObject.put(limitParamName, limit);

									boolean isResultSetCompleted = true;
									if( !webServiceApi.getValidateOrPreview() )
									{
										String soapBody = webServiceApi.getSoapBodyElement();
										soapBody = StringUtils.replace(soapBody, limitParamName, limitParamValue);
										webServiceApi.setSoapBodyElement(soapBody);
										getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), paginationObjectName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										if( dataList != null )
										{
											LinkedHashMap<String, Object> searchIdMap = (LinkedHashMap<String, Object>) dataList.get(0);
											Set<String> set = searchIdMap.keySet();
											for (String key : set)
											{
												searchId = (String) searchIdMap.get(key);
											}
										}
										dataList.clear();
									}
									if( webServiceApi.getValidateOrPreview() )
									{
										String soapBody = webServiceApi.getSoapBodyElement();
										soapBody = StringUtils.replace(soapBody, limitParamName, limitParamValue);
										webServiceApi.setSoapBodyElement(soapBody);
										getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
									}
									else
									{
										while (isResultSetCompleted)
										{
											paginationSoapBody = StringUtils.replace(paginationSoapBody, offsetParamName, offsetParamValue);
											paginationSoapBody = StringUtils.replace(paginationSoapBody, paginationSearchId, searchId);
											webServiceApi.setSoapBodyElement(paginationSoapBody);
											headers = new HttpHeaders();
											headers.add("Content-Type", "text/plain");
											headers.add("SOAPAction", "searchMoreWithId");
											isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
											offset = offset + 1;
											offsetParamValue = String.valueOf(offset);

											if( dataList.size() > 0 )
											{
												Connection connection = null;
												try
												{
													connection = getStagingConnection(clientDbDetails);
													System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
													formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
													formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
													System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

													String tableName = webServiceApi.getTable().getTableName();

													if( tableName == null )
													{
														tableName = getTempTableName(formattedApiResponse, connection);
														webServiceApi.getTable().setTableName(tableName);
													}

													List<Column> columns = getTableStructure(tableName, connection);
													Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

													List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
													webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
													writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
													dataList.clear();
												}
												finally
												{
													closeConnection(connection);
												}
											}
											else
											{
												String tableName = webServiceApi.getTable().getTableName();
												if( tableName == null )
												{
													createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
												}
											}
										}
									}
								}

							}

						}
						else
						{

							getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
							if( !webServiceApi.getValidateOrPreview() )
							{
								if( dataList.size() > 0 )
								{
									Connection connection = null;
									try
									{
										connection = getStagingConnection(clientDbDetails);
										System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
										formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
										formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
										System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

										String tableName = webServiceApi.getTable().getTableName();

										if( tableName == null )
										{
											tableName = getTempTableName(formattedApiResponse, connection);
											webServiceApi.getTable().setTableName(tableName);
										}

										List<Column> columns = getTableStructure(tableName, connection);
										Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

										List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
										webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
										writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
										dataList.clear();
									}
									finally
									{
										closeConnection(connection);
									}
								}
								else
								{
									String tableName = webServiceApi.getTable().getTableName();
									if( tableName == null )
									{
										createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
									}
								}
							}
						}
						if( webServiceApi.getValidateOrPreview() )
						{
							break;
						}
					}
					/* rest web service calling... */
					else
					{
						if( webServiceApi.getPaginationRequired() && StringUtils.isNotBlank(webServiceApi.getPaginationRequestParamsData()) && StringUtils.startsWith(webServiceApi.getPaginationRequestParamsData(), "[") )
						{
							/* offset pagination */
							if( webServiceApi.getPaginationType().equals(WebServiceConstants.PaginationType.OFFSET.type) )
							{
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								for (int i = 0; i < paramters.length(); i++)
								{
									JSONObject paramObject = paramters.getJSONObject(i);
									String offsetParamName = paramObject.getString("paginationOffSetRequestParamName");
									String offsetParamValue = paramObject.getString("paginationOffSetRequestParamValue");
									String limitParamName = paramObject.getString("paginationLimitRequestParamName");
									String limitParamValue = paramObject.getString("paginationLimitRequestParamValue");
									String paginationParamType = paramObject.getString("paginationParamType");
									int limit = Integer.parseInt(limitParamValue);
									int offset = Integer.parseInt(offsetParamValue);
									if( webServiceApi.getRetryPaginationData() != null )
									{
										JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
										int retryOffset = retryPaginationDataObject.getInt("retryOffset");
										offset = retryOffset;
										String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
										webServiceApi.getTable().setTableName(retryTempTableName);
									}
									/*
									 * offset pagination for Request Parameter
									 */
									if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
									{
										url += (url.contains("?") ? "&" : "?") + limitParamName + "=" + limit + "&" + offsetParamName + "=";
										boolean isResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											getResponseObject(webServiceApi, url + offset, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											while (isResultSetCompleted)
											{
												LOG.info("Initiating call for uuid : " + uuid.toString() + "  offset -> " + offset + "   limit -> " + limit);
												isResultSetCompleted = getResponseObject(webServiceApi, url + offset, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, clientDbDetails);
												LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
												offset += limit;
												webServiceApi.setRetryOffset(offset);
												if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
												{
													if( dataList.size() > 0 )
													{
														Connection connection = null;
														try
														{
															connection = getStagingConnection(clientDbDetails);
															System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
															formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
															formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
															System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

															String tableName = webServiceApi.getTable().getTableName();

															if( tableName == null )
															{
																tableName = getTempTableName(formattedApiResponse, connection);
																webServiceApi.getTable().setTableName(tableName);
															}

															List<Column> columns = getTableStructure(tableName, connection);
															Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

															List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
															webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
															writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
															dataList.clear();
														}
														finally
														{
															closeConnection(connection);
														}
													}
													else
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
												{
													if( dataList.size() > 0 )
													{
														FileWriter jsonfileWriter = null;
														try
														{
															System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
															String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
															if( filePath != null )
															{
																jsonfileWriter = new FileWriter(jsonFilePath);
																writeObjectToJson(jsonfileWriter, dataList, false);
															}
															System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
														finally
														{
															closeJsonfileWriter(jsonfileWriter);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
												{
													System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
													writeObjectToXml(filePath, dataList);
													System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
													dataList.clear();
												}
											}
										}
									}
									else
									{
										/*
										 * offset pagination for Body Parameter
										 */
										postBodyObject.put(offsetParamName, offset);
										postBodyObject.put(limitParamName, limit);

										boolean isResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											while (isResultSetCompleted)
											{
												LOG.info("Initiating call for uuid : " + uuid.toString() + "  offset -> " + offset + "   limit -> " + limit);
												isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
												LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
												offset += limit;
												postBodyObject.put(offsetParamName, offset);
												postBodyObject.put(limitParamName, limit);
												webServiceApi.setRetryOffset(offset);
												if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
												{
													if( dataList.size() > 0 )
													{
														Connection connection = null;
														try
														{
															connection = getStagingConnection(clientDbDetails);
															System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
															formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
															formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
															System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

															String tableName = webServiceApi.getTable().getTableName();

															if( tableName == null )
															{
																tableName = getTempTableName(formattedApiResponse, connection);
																webServiceApi.getTable().setTableName(tableName);
															}

															List<Column> columns = getTableStructure(tableName, connection);
															Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

															List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
															webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
															writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
															dataList.clear();
														}
														finally
														{
															closeConnection(connection);
														}
													}
													else
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
												{
													if( dataList.size() > 0 )
													{
														FileWriter jsonfileWriter = null;
														try
														{
															System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
															String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
															if( filePath != null )
															{
																jsonfileWriter = new FileWriter(jsonFilePath);
																writeObjectToJson(jsonfileWriter, dataList, false);
															}
															System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
														finally
														{
															closeJsonfileWriter(jsonfileWriter);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
												{
													System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
													writeObjectToXml(filePath, dataList);
													System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
													dataList.clear();
												}
											}
										}
									}

								}
							}
							else if( webServiceApi.getPaginationType().equals(WebServiceConstants.PaginationType.PAGE.type) )
							{
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								for (int i = 0; i < paramters.length(); i++)
								{
									JSONObject paramObject = paramters.getJSONObject(i);
									String pageNumberParamName = paramObject.getString("paginationPageNumberRequestParamName");
									String pageNumberParamValue = paramObject.getString("paginationPageNumberRequestParamValue");
									String pageSizeParamName = paramObject.getString("paginationPageSizeRequestParamName");
									String pageSizeParamValue = paramObject.getString("paginationPageSizeRequestParamValue");
									String paginationParamType = paramObject.getString("paginationParamType");
									int pageNumber = Integer.parseInt(pageNumberParamValue);
									int pageSize = Integer.parseInt(pageNumberParamValue);
									if( webServiceApi.getRetryPaginationData() != null )
									{
										JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
										int retryPage = retryPaginationDataObject.getInt("retryPage");
										pageNumber = retryPage;
										String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
										webServiceApi.getTable().setTableName(retryTempTableName);
									}
									/*
									 * page number and size pagination for
									 * Request Parameter
									 */
									if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
									{
										url += (url.contains("?") ? "&" : "?") + pageSizeParamName + "=" + pageSizeParamValue + "&" + pageNumberParamName + "=";
										boolean isResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											getResponseObject(webServiceApi, url + pageNumber, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											while (isResultSetCompleted)
											{
												LOG.info("Initiating call for uuid : " + uuid.toString() + "  Page Number -> " + pageNumber + "   Page Size -> " + pageSize);
												isResultSetCompleted = getResponseObject(webServiceApi, url + pageNumber, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
												LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
												pageNumber = pageNumber + 1;
												webServiceApi.setRetryPage(pageNumber);
												if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
												{
													if( dataList.size() > 0 )
													{
														Connection connection = null;
														try
														{
															connection = getStagingConnection(clientDbDetails);
															System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
															formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
															formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
															System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

															String tableName = webServiceApi.getTable().getTableName();

															if( tableName == null )
															{
																tableName = getTempTableName(formattedApiResponse, connection);
																webServiceApi.getTable().setTableName(tableName);
															}

															List<Column> columns = getTableStructure(tableName, connection);
															Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

															List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
															webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
															writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
															dataList.clear();
														}
														finally
														{
															closeConnection(connection);
														}
													}
													else
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
												{
													if( dataList.size() > 0 )
													{
														FileWriter jsonfileWriter = null;
														try
														{
															System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
															String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
															if( filePath != null )
															{
																jsonfileWriter = new FileWriter(jsonFilePath);
																writeObjectToJson(jsonfileWriter, dataList, false);
															}
															System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
														finally
														{
															closeJsonfileWriter(jsonfileWriter);
														}
													}

												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
												{
													System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
													writeObjectToXml(filePath, dataList);
													System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
													dataList.clear();
												}
											}
										}
									}
									else
									{
										/*
										 * page number and size pagination for
										 * Body Parameter
										 */
										postBodyObject.put(pageNumberParamName, pageNumber);
										postBodyObject.put(pageSizeParamName, pageSize);

										boolean isResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											while (isResultSetCompleted)
											{
												LOG.info("Initiating call for uuid : " + uuid.toString() + "  Page Number -> " + pageNumber + "   Page Size -> " + pageSize);
												isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
												LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
												pageNumber = pageNumber + 1;
												postBodyObject.put(pageNumberParamName, pageNumber);
												postBodyObject.put(pageSizeParamName, pageSize);
												webServiceApi.setRetryPage(pageNumber);

												if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
												{
													if( dataList.size() > 0 )
													{
														Connection connection = null;
														try
														{
															connection = getStagingConnection(clientDbDetails);
															System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
															formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
															formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
															System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

															String tableName = webServiceApi.getTable().getTableName();

															if( tableName == null )
															{
																tableName = getTempTableName(formattedApiResponse, connection);
																webServiceApi.getTable().setTableName(tableName);
															}

															List<Column> columns = getTableStructure(tableName, connection);
															Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

															List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
															webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
															writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
															dataList.clear();
														}
														finally
														{
															closeConnection(connection);
														}
													}
													else
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
												{
													if( dataList.size() > 0 )
													{
														FileWriter jsonfileWriter = null;
														try
														{
															System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
															String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
															if( filePath != null )
															{
																jsonfileWriter = new FileWriter(jsonFilePath);
																writeObjectToJson(jsonfileWriter, dataList, false);
															}
															System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
														finally
														{
															closeJsonfileWriter(jsonfileWriter);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
												{
													System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
													writeObjectToXml(filePath, dataList);
													System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
													dataList.clear();
												}
											}
										}
									}

								}
							}
							else if( webServiceApi.getPaginationType().equals(WebServiceConstants.PaginationType.DATE.type) )
							{
								/* date pagination */
								String retryStartDate = null;
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								for (int i = 0; i < paramters.length(); i++)
								{
									JSONObject paramObject = paramters.getJSONObject(i);
									String paginationStartDateParam = paramObject.getString("paginationStartDateParam");
									String paginationEndDateParam = paramObject.getString("paginationEndDateParam");
									int paginationDateRange = Integer.parseInt(paramObject.getString("paginationDateRange"));
									String paginationParamType = paramObject.getString("paginationParamType");
									String paginationStartDate = paramObject.getString("paginationStartDate");

									String datePageNumberParamName = getJSONObjectKey(paramObject, "paginationDatePageNumberRequestParamName");
									String datePageNumberParamValue = getJSONObjectKey(paramObject, "paginationDatePageNumberRequestParamValue");
									String datePageSizeParamName = getJSONObjectKey(paramObject, "paginationDatePageSizeRequestParamName");
									String datePageSizeParamValue = getJSONObjectKey(paramObject, "paginationDatePageSizeRequestParamValue");

									int datePageNumber = 0;
									int datePageSize = 0;
									if( StringUtils.isNotBlank(datePageNumberParamName) && StringUtils.isNotBlank(datePageSizeParamName) )
									{
										datePageNumber = Integer.parseInt(datePageNumberParamValue);
										datePageSize = Integer.parseInt(datePageSizeParamValue);
									}
									if( webServiceApi.getRetryPaginationData() != null )
									{
										JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
										retryStartDate = retryPaginationDataObject.getString("retryStartDate");
										paginationStartDate = retryStartDate;

										String retryDatePage = getJSONObjectKey(retryPaginationDataObject, "retryDatePage");
										if( StringUtils.isNotBlank(datePageNumberParamName) )
										{
											int retryDatePageNumberParamValue = Integer.parseInt(retryDatePage);
											datePageNumber = retryDatePageNumberParamValue;
										}
										String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
										webServiceApi.getTable().setTableName(retryTempTableName);
									}

									/* date pagination for Request Parameter */
									if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
									{
										String startDate = null;
										if( retryStartDate == null )
										{
											startDate = convertDateFormat(dateFormat, paginationStartDate, timeZone);
										}
										else
										{
											startDate = paginationStartDate;
										}
										String paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
										url += (url.contains("?") ? "&" : "?") + paginationStartDateParam + "=%s" + "&" + paginationEndDateParam + "=%s";
										boolean isResultSetCompleted = true;
										boolean isDatePageNumerSizeResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);

											if( StringUtils.isNotBlank(datePageNumberParamName) && StringUtils.isNotBlank(datePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												url += "&" + datePageSizeParamName + "=" + datePageSize + "&" + datePageNumberParamName + "=%s";
												while (isResultSetCompleted)
												{
													while (isDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days -> " + " date Page Number -> " + datePageNumber + " date Page Size -> "
																+ datePageSize);
														isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate, datePageNumber), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList,
																autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());

														datePageNumber = datePageNumber + 1;
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isDatePageNumerSizeResultSetCompleted = true;
														}
													}
													datePageNumber = 0;
													isDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);

													String toDayDate = getToDate();
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers,
															restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);

													String toDayDate = getToDate();
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(datePageNumberParamName) && StringUtils.isNotBlank(datePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												url += "&" + datePageSizeParamName + "=" + datePageSize + "&" + datePageNumberParamName + "=%s";
												while (isResultSetCompleted)
												{
													while (isDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days -> " + " date Page Number -> " + datePageNumber + " date Page Size -> "
																+ datePageSize);
														isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate, datePageNumber), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList,
																autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														datePageNumber = datePageNumber + 1;
														webServiceApi.setRetryDatePage(datePageNumber);
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															if( dataList.size() > 0 )
															{
																Connection connection = null;
																try
																{
																	connection = getStagingConnection(clientDbDetails);
																	System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																	formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																	formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																	System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																	String tableName = webServiceApi.getTable().getTableName();

																	if( tableName == null )
																	{
																		tableName = getTempTableName(formattedApiResponse, connection);
																		webServiceApi.getTable().setTableName(tableName);
																	}

																	List<Column> columns = getTableStructure(tableName, connection);
																	Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																	List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																	webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																	writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																	dataList.clear();
																}
																finally
																{
																	closeConnection(connection);
																}
															}
															else
															{
																String tableName = webServiceApi.getTable().getTableName();
																if( tableName == null )
																{
																	createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
														{
															if( dataList.size() > 0 )
															{
																FileWriter jsonfileWriter = null;
																try
																{
																	System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																	String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																	if( filePath != null )
																	{
																		jsonfileWriter = new FileWriter(jsonFilePath);
																		writeObjectToJson(jsonfileWriter, dataList, false);
																	}
																	System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																	dataList.clear();
																}
																finally
																{
																	closeJsonfileWriter(jsonfileWriter);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
														{
															System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
															writeObjectToXml(filePath, dataList);
															System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
													}
													datePageNumber = 0;
													isDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
													webServiceApi.setRetryDate(startDate);
													String toDayDate = getToDate();
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}

												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers,
															restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													webServiceApi.setRetryDate(startDate);
													if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
													{
														if( dataList.size() > 0 )
														{
															Connection connection = null;
															try
															{
																connection = getStagingConnection(clientDbDetails);
																System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																String tableName = webServiceApi.getTable().getTableName();

																if( tableName == null )
																{
																	tableName = getTempTableName(formattedApiResponse, connection);
																	webServiceApi.getTable().setTableName(tableName);
																}

																List<Column> columns = getTableStructure(tableName, connection);
																Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																dataList.clear();
															}
															finally
															{
																closeConnection(connection);
															}
														}
														else
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
													{
														if( dataList.size() > 0 )
														{
															FileWriter jsonfileWriter = null;
															try
															{
																System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																if( filePath != null )
																{
																	jsonfileWriter = new FileWriter(jsonFilePath);
																	writeObjectToJson(jsonfileWriter, dataList, false);
																}
																System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																dataList.clear();
															}
															finally
															{
																closeJsonfileWriter(jsonfileWriter);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
													{
														System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
														writeObjectToXml(filePath, dataList);
														System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
														dataList.clear();
													}
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
													String toDayDate = getToDate();
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
										}

									}
									else
									{
										/* date pagination for Body Parameter */
										String startDate = null;
										if( retryStartDate == null )
										{
											startDate = convertDateFormat(dateFormat, paginationStartDate, timeZone);
										}
										else
										{
											startDate = paginationStartDate;
										}
										String paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
										postBodyObject.put(paginationStartDateParam, startDate);
										postBodyObject.put(paginationEndDateParam, paginationEndDate);

										boolean isResultSetCompleted = true;
										boolean isDatePageNumerSizeResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(datePageNumberParamName) && StringUtils.isNotBlank(datePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												while (isResultSetCompleted)
												{
													postBodyObject.put(datePageSizeParamName, datePageSize);
													postBodyObject.put(datePageNumberParamName, datePageNumber);

													while (isDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days -> " + " date Page Number -> " + datePageNumber + " date Page Size -> "
																+ datePageSize);
														isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														datePageNumber = datePageNumber + 1;
														webServiceApi.setRetryDatePage(datePageNumber);
														postBodyObject.put(datePageSizeParamName, datePageSize);
														postBodyObject.put(datePageNumberParamName, datePageNumber);
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isDatePageNumerSizeResultSetCompleted = true;
														}
													}
													datePageNumber = 0;
													isDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);

													postBodyObject.put(paginationStartDateParam, startDate);
													postBodyObject.put(paginationEndDateParam, paginationEndDate);

													String toDayDate = getToDate();
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);

													postBodyObject.put(paginationStartDateParam, startDate);
													postBodyObject.put(paginationEndDateParam, paginationEndDate);

													String toDayDate = getToDate();
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(datePageNumberParamName) && StringUtils.isNotBlank(datePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												while (isResultSetCompleted)
												{
													postBodyObject.put(datePageSizeParamName, datePageSize);
													postBodyObject.put(datePageNumberParamName, datePageNumber);
													while (isDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days-> " + " date Page Number -> " + datePageNumber + " date Page Size -> "
																+ datePageSize);
														isDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														datePageNumber = datePageNumber + 1;
														webServiceApi.setRetryDatePage(datePageNumber);
														webServiceApi.setRetryDate(startDate);
														postBodyObject.put(datePageSizeParamName, datePageSize);
														postBodyObject.put(datePageNumberParamName, datePageNumber);
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															if( dataList.size() > 0 )
															{
																Connection connection = null;
																try
																{
																	connection = getStagingConnection(clientDbDetails);
																	System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																	formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																	formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																	System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																	String tableName = webServiceApi.getTable().getTableName();

																	if( tableName == null )
																	{
																		tableName = getTempTableName(formattedApiResponse, connection);
																		webServiceApi.getTable().setTableName(tableName);
																	}

																	List<Column> columns = getTableStructure(tableName, connection);
																	Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																	List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																	webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																	writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																	dataList.clear();
																}
																finally
																{
																	closeConnection(connection);
																}
															}
															else
															{
																String tableName = webServiceApi.getTable().getTableName();
																if( tableName == null )
																{
																	createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
														{
															if( dataList.size() > 0 )
															{
																FileWriter jsonfileWriter = null;
																try
																{
																	System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																	String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																	if( filePath != null )
																	{
																		jsonfileWriter = new FileWriter(jsonFilePath);
																		writeObjectToJson(jsonfileWriter, dataList, false);
																	}
																	System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																	dataList.clear();
																}
																finally
																{
																	closeJsonfileWriter(jsonfileWriter);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
														{
															System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
															writeObjectToXml(filePath, dataList);
															System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
													}
													datePageNumber = 0;
													isDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
													/*
													 * updating body param's
													 * with updated dates.
													 * 
													 */
													postBodyObject.put(paginationStartDateParam, startDate);
													postBodyObject.put(paginationEndDateParam, paginationEndDate);
													String toDayDate = getToDate();
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}

											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													webServiceApi.setRetryDate(startDate);
													/*
													 * updating body param's
													 * with updated dates.
													 * 
													 */
													postBodyObject.put(paginationStartDateParam, startDate);
													postBodyObject.put(paginationEndDateParam, paginationEndDate);
													if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
													{
														if( dataList.size() > 0 )
														{
															Connection connection = null;
															try
															{
																connection = getStagingConnection(clientDbDetails);
																System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																String tableName = webServiceApi.getTable().getTableName();

																if( tableName == null )
																{
																	tableName = getTempTableName(formattedApiResponse, connection);
																	webServiceApi.getTable().setTableName(tableName);
																}

																List<Column> columns = getTableStructure(tableName, connection);
																Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																dataList.clear();
															}
															finally
															{
																closeConnection(connection);
															}
														}
														else
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
													{
														if( dataList.size() > 0 )
														{
															FileWriter jsonfileWriter = null;
															try
															{
																System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																if( filePath != null )
																{
																	jsonfileWriter = new FileWriter(jsonFilePath);
																	writeObjectToJson(jsonfileWriter, dataList, false);
																}
																System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																dataList.clear();
															}
															finally
															{
																closeJsonfileWriter(jsonfileWriter);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
													{
														System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
														writeObjectToXml(filePath, dataList);
														System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
														dataList.clear();
													}
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationDateRange);
													String toDayDate = getToDate();
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
										}
									}
								}
							}
							else if( webServiceApi.getPaginationType().equals(WebServiceConstants.PaginationType.INCREMENTAL_DATE.type) )
							{
								/* incremental date pagination */
								String retryIncrementalStartDate = null;
								String retryIncrementalEndDate = null;
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								for (int i = 0; i < paramters.length(); i++)
								{
									JSONObject paramObject = paramters.getJSONObject(i);
									String paginationIncrementalStartDateParam = paramObject.getString("paginationIncrementalStartDateParam");
									String paginationIncrementalStartDate = paramObject.getString("paginationIncrementalStartDate");
									String paginationIncrementalEndDateParam = paramObject.getString("paginationIncrementalEndDateParam");
									String paginationParamType = paramObject.getString("paginationParamType");
									String paginationIncrementalEndDate = paramObject.getString("paginationIncrementalEndDate");

									String incrementalDatePageNumberParamName = getJSONObjectKey(paramObject, "paginationIncrementalDatePageNumberRequestParamName");
									String incrementalDatePageNumberParamValue = getJSONObjectKey(paramObject, "paginationIncrementalDatePageNumberRequestParamValue");
									String incrementalDatePageSizeParamName = getJSONObjectKey(paramObject, "paginationIncrementalDatePageSizeRequestParamName");
									String incrementalDatePageSizeParamValue = getJSONObjectKey(paramObject, "paginationIncrementalDatePageSizeRequestParamValue");

									int incrementalDatePageNumber = 0;
									int incrementalDatePageSize = 0;
									if( StringUtils.isNotBlank(incrementalDatePageNumberParamName) && StringUtils.isNotBlank(incrementalDatePageSizeParamName) )
									{
										incrementalDatePageNumber = Integer.parseInt(incrementalDatePageNumberParamValue);
										incrementalDatePageSize = Integer.parseInt(incrementalDatePageSizeParamValue);
									}
									if( StringUtils.isEmpty(paginationIncrementalEndDate) )
									{
										paginationIncrementalEndDate = getToDate();
									}
									int paginationIncrementalDateRange = Integer.parseInt(paramObject.getString("paginationIncrementalDateRange"));
									if( webServiceApi.getRetryPaginationData() != null )
									{
										JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
										retryIncrementalStartDate = retryPaginationDataObject.getString("retryIncrementalStartDate");
										paginationIncrementalStartDate = retryIncrementalStartDate;
										retryIncrementalEndDate = retryPaginationDataObject.getString("retryIncrementalEndDate");
										paginationIncrementalEndDate = retryIncrementalEndDate;

										String retryIncrementalDatePage = getJSONObjectKey(retryPaginationDataObject, "retryIncrementalDatePage");
										if( StringUtils.isNotBlank(retryIncrementalDatePage) )
										{
											int retryDatePageNumberParamValue = Integer.parseInt(retryIncrementalDatePage);
											incrementalDatePageNumber = retryDatePageNumberParamValue;
										}

										String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
										webServiceApi.getTable().setTableName(retryTempTableName);
									}

									/*
									 * incremental date pagination for Request
									 * Parameter
									 */
									if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
									{
										String startDate = null;
										if( retryIncrementalStartDate == null )
										{
											startDate = convertDateFormat(dateFormat, paginationIncrementalStartDate, timeZone);
										}
										else
										{
											startDate = paginationIncrementalStartDate;
										}

										String paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);
										url += (url.contains("?") ? "&" : "?") + paginationIncrementalStartDateParam + "=%s" + "&" + paginationIncrementalEndDateParam + "=%s";
										boolean isResultSetCompleted = true;
										boolean isIncrementalDatePageNumerSizeResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(incrementalDatePageNumberParamName) && StringUtils.isNotBlank(incrementalDatePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												url += "&" + incrementalDatePageSizeParamName + "=" + incrementalDatePageSize + "&" + incrementalDatePageNumberParamName + "=%s";
												while (isResultSetCompleted)
												{
													while (isIncrementalDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days-> " + " incremental date Page Number -> "
																+ incrementalDatePageNumber + " incremental  date Page Size -> " + incrementalDatePageSize);
														isIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate, incrementalDatePageNumber), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(),
																postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());

														incrementalDatePageNumber = incrementalDatePageNumber + 1;
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isIncrementalDatePageNumerSizeResultSetCompleted = true;
														}
													}
													incrementalDatePageNumber = 0;
													isIncrementalDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);

													String toDayDate = paginationIncrementalEndDate;
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers,
															restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);

													String toDayDate = paginationIncrementalEndDate;
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(incrementalDatePageNumberParamName) && StringUtils.isNotBlank(incrementalDatePageSizeParamName) )
											{
												/*
												 * date pagination page number
												 * and size
												 */
												url += "&" + incrementalDatePageSizeParamName + "=" + incrementalDatePageSize + "&" + incrementalDatePageNumberParamName + "=%s";
												while (isResultSetCompleted)
												{
													while (isIncrementalDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days -> " + " incremental date Page Number -> "
																+ incrementalDatePageNumber + " incremental  date Page Size -> " + incrementalDatePageSize);
														isIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate, incrementalDatePageNumber), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(),
																postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
														incrementalDatePageNumber = incrementalDatePageNumber + 1;
														webServiceApi.setRetryIncrementalDatePage(incrementalDatePageNumber);
														webServiceApi.setRetryIncrementalStartDate(startDate);
														webServiceApi.setRetryIncrementalEndDate(paginationIncrementalEndDate);
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															if( dataList.size() > 0 )
															{
																Connection connection = null;
																try
																{
																	connection = getStagingConnection(clientDbDetails);
																	System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																	formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																	formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																	System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																	String tableName = webServiceApi.getTable().getTableName();

																	if( tableName == null )
																	{
																		tableName = getTempTableName(formattedApiResponse, connection);
																		webServiceApi.getTable().setTableName(tableName);
																	}

																	List<Column> columns = getTableStructure(tableName, connection);
																	Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																	List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																	webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																	writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																	dataList.clear();
																}
																finally
																{
																	closeConnection(connection);
																}
															}
															else
															{
																String tableName = webServiceApi.getTable().getTableName();
																if( tableName == null )
																{
																	createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
																}
															}

														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
														{
															if( dataList.size() > 0 )
															{
																FileWriter jsonfileWriter = null;
																try
																{
																	System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																	String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																	if( filePath != null )
																	{
																		jsonfileWriter = new FileWriter(jsonFilePath);
																		writeObjectToJson(jsonfileWriter, dataList, false);
																	}
																	System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																	dataList.clear();
																}
																finally
																{
																	closeJsonfileWriter(jsonfileWriter);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
														{
															System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
															writeObjectToXml(filePath, dataList);
															System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
													}
													incrementalDatePageNumber = 0;
													isIncrementalDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);
													String toDayDate = paginationIncrementalEndDate;
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}

											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers,
															restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													webServiceApi.setRetryIncrementalStartDate(startDate);
													webServiceApi.setRetryIncrementalEndDate(paginationIncrementalEndDate);
													if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
													{
														if( dataList.size() > 0 )
														{
															Connection connection = null;
															try
															{
																connection = getStagingConnection(clientDbDetails);
																System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																String tableName = webServiceApi.getTable().getTableName();

																if( tableName == null )
																{
																	tableName = getTempTableName(formattedApiResponse, connection);
																	webServiceApi.getTable().setTableName(tableName);
																}

																List<Column> columns = getTableStructure(tableName, connection);
																Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																dataList.clear();
															}
															finally
															{
																closeConnection(connection);
															}
														}
														else
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
													{
														if( dataList.size() > 0 )
														{
															FileWriter jsonfileWriter = null;
															try
															{
																System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																if( filePath != null )
																{
																	jsonfileWriter = new FileWriter(jsonFilePath);
																	writeObjectToJson(jsonfileWriter, dataList, false);
																}
																System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																dataList.clear();
															}
															finally
															{
																closeJsonfileWriter(jsonfileWriter);
															}
														}

													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
													{
														System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
														writeObjectToXml(filePath, dataList);
														System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
														dataList.clear();
													}
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);
													String toDayDate = paginationIncrementalEndDate;
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
										}

									}
									else
									{
										/*
										 * Incremental date pagination for Body
										 * Parameter
										 */
										String startDate = null;
										if( retryIncrementalStartDate == null )
										{
											startDate = convertDateFormat(dateFormat, paginationIncrementalStartDate, timeZone);
										}
										else
										{
											startDate = paginationIncrementalStartDate;
										}

										String paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);
										postBodyObject.put(paginationIncrementalStartDateParam, startDate);
										postBodyObject.put(paginationIncrementalEndDateParam, paginationEndDate);

										boolean isResultSetCompleted = true;
										boolean isIncrementalDatePageNumerSizeResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(incrementalDatePageNumberParamName) && StringUtils.isNotBlank(incrementalDatePageSizeParamName) )
											{
												/*
												 * incremental date pagination
												 * page number and size
												 */
												while (isResultSetCompleted)
												{
													postBodyObject.put(incrementalDatePageSizeParamName, incrementalDatePageSize);
													postBodyObject.put(incrementalDatePageNumberParamName, incrementalDatePageNumber);

													while (isIncrementalDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days-> " + " incremental date Page Number -> "
																+ incrementalDatePageNumber + " incremental  date Page Size -> " + incrementalDatePageSize);
														isIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
																clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());

														incrementalDatePageNumber = incrementalDatePageNumber + 1;

														postBodyObject.put(incrementalDatePageSizeParamName, incrementalDatePageSize);
														postBodyObject.put(incrementalDatePageNumberParamName, incrementalDatePageNumber);

														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isIncrementalDatePageNumerSizeResultSetCompleted = true;
														}
													}
													incrementalDatePageNumber = 0;
													isIncrementalDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);

													postBodyObject.put(paginationIncrementalStartDateParam, startDate);
													postBodyObject.put(paginationIncrementalEndDateParam, paginationEndDate);

													String toDayDate = paginationIncrementalEndDate;
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);

													postBodyObject.put(paginationIncrementalStartDateParam, startDate);
													postBodyObject.put(paginationIncrementalEndDateParam, paginationEndDate);

													String toDayDate = paginationIncrementalEndDate;
													if( compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat) )
													{
														break;
													}
													else
													{
														if( dataList.size() > 0 )
														{
															break;
														}
														else
														{
															isResultSetCompleted = true;
														}
													}
												}
											}
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											if( StringUtils.isNotBlank(incrementalDatePageNumberParamName) && StringUtils.isNotBlank(incrementalDatePageSizeParamName) )
											{
												/*
												 * incremental date pagination
												 * page number and size
												 */
												while (isResultSetCompleted)
												{
													postBodyObject.put(incrementalDatePageSizeParamName, incrementalDatePageSize);
													postBodyObject.put(incrementalDatePageNumberParamName, incrementalDatePageNumber);

													while (isIncrementalDatePageNumerSizeResultSetCompleted)
													{
														LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days-> " + " incremental date Page Number -> "
																+ incrementalDatePageNumber + " incremental  date Page Size -> " + incrementalDatePageSize);
														isIncrementalDatePageNumerSizeResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
																clientDbDetails);
														LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());

														incrementalDatePageNumber = incrementalDatePageNumber + 1;
														webServiceApi.setRetryIncrementalDatePage(incrementalDatePageNumber);
														webServiceApi.setRetryIncrementalStartDate(startDate);
														webServiceApi.setRetryIncrementalEndDate(paginationIncrementalEndDate);
														postBodyObject.put(incrementalDatePageSizeParamName, incrementalDatePageSize);
														postBodyObject.put(incrementalDatePageNumberParamName, incrementalDatePageNumber);
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															if( dataList.size() > 0 )
															{
																Connection connection = null;
																try
																{
																	connection = getStagingConnection(clientDbDetails);
																	System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																	formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																	formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																	System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																	String tableName = webServiceApi.getTable().getTableName();

																	if( tableName == null )
																	{
																		tableName = getTempTableName(formattedApiResponse, connection);
																		webServiceApi.getTable().setTableName(tableName);
																	}

																	List<Column> columns = getTableStructure(tableName, connection);
																	Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																	List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																	webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																	writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																	dataList.clear();
																}
																finally
																{
																	closeConnection(connection);
																}
															}
															else
															{
																String tableName = webServiceApi.getTable().getTableName();
																if( tableName == null )
																{
																	createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
														{
															if( dataList.size() > 0 )
															{
																FileWriter jsonfileWriter = null;
																try
																{
																	System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																	String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																	if( filePath != null )
																	{
																		jsonfileWriter = new FileWriter(jsonFilePath);
																		writeObjectToJson(jsonfileWriter, dataList, false);
																	}
																	System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																	dataList.clear();
																}
																finally
																{
																	closeJsonfileWriter(jsonfileWriter);
																}
															}
														}
														else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
														{
															System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
															writeObjectToXml(filePath, dataList);
															System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
													}
													incrementalDatePageNumber = 0;
													isIncrementalDatePageNumerSizeResultSetCompleted = true;
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);

													/*
													 * updating body params with
													 * updated dates.
													 * 
													 */
													postBodyObject.put(paginationIncrementalStartDateParam, startDate);
													postBodyObject.put(paginationIncrementalEndDateParam, paginationEndDate);
													String toDayDate = paginationIncrementalEndDate;
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);
													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
											else
											{
												while (isResultSetCompleted)
												{
													LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + paginationIncrementalDateRange + " days");
													isResultSetCompleted = getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
													LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
													webServiceApi.setRetryIncrementalStartDate(startDate);
													webServiceApi.setRetryIncrementalEndDate(paginationIncrementalEndDate);
													/*
													 * updating body params with
													 * updated dates.
													 * 
													 */
													postBodyObject.put(paginationIncrementalStartDateParam, startDate);
													postBodyObject.put(paginationIncrementalEndDateParam, paginationEndDate);
													if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
													{
														if( dataList.size() > 0 )
														{
															Connection connection = null;
															try
															{
																connection = getStagingConnection(clientDbDetails);
																System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
																formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
																formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
																System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

																String tableName = webServiceApi.getTable().getTableName();

																if( tableName == null )
																{
																	tableName = getTempTableName(formattedApiResponse, connection);
																	webServiceApi.getTable().setTableName(tableName);
																}

																List<Column> columns = getTableStructure(tableName, connection);
																Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

																List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
																webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
																writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
																dataList.clear();
															}
															finally
															{
																closeConnection(connection);
															}
														}
														else
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
													{
														if( dataList.size() > 0 )
														{
															FileWriter jsonfileWriter = null;
															try
															{
																System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
																String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
																if( filePath != null )
																{
																	jsonfileWriter = new FileWriter(jsonFilePath);
																	writeObjectToJson(jsonfileWriter, dataList, false);
																}
																System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
																dataList.clear();
															}
															finally
															{
																closeJsonfileWriter(jsonfileWriter);
															}
														}

													}
													else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
													{
														System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
														writeObjectToXml(filePath, dataList);
														System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
														dataList.clear();
													}
													startDate = paginationEndDate;
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, paginationIncrementalDateRange);
													String toDayDate = paginationIncrementalEndDate;
													boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDayDate, timeZone), dateFormat);

													if( compareDates )
													{
														if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
														{
															String tableName = webServiceApi.getTable().getTableName();
															if( tableName == null )
															{
																createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
															}
														}
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
										}
									}
								}
							}
							else if( webServiceApi.getPaginationType().equals(WebServiceConstants.PaginationType.CONDITIONAL_DATE.type) )
							{
								/* conditional date pagination */
								String retryStartDate = null;
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								for (int i = 0; i < paramters.length(); i++)
								{
									JSONObject paramObject = paramters.getJSONObject(i);
									String filterName = paramObject.getString("paginationFilterName");
									String fromDateParam = paramObject.getString("paginationConditionalFromDateParam");
									String fromDateCondition = paramObject.getString("paginationConditionalFromDateCondition");
									String fromDate = paramObject.getString("paginationConditionalFromDate");
									String conditionalParam = paramObject.getString("paginationConditionalParam");
									String toDateParam = paramObject.getString("paginationConditionalToDateParam");
									String toDateCondition = paramObject.getString("paginationConditionalToDateCondition");
									String toDate = paramObject.getString("paginationConditionalToDate");
									if( StringUtils.isEmpty(toDate) )
									{
										toDate = getToDate();
									}
									String paginationParamType = paramObject.getString("paginationParamType");
									int dayRange = Integer.parseInt(paramObject.getString("paginationConditionalDayRange"));
									if( webServiceApi.getRetryPaginationData() != null )
									{
										JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
										retryStartDate = retryPaginationDataObject.getString("retryStartDate");
										fromDate = retryStartDate;
										String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
										webServiceApi.getTable().setTableName(retryTempTableName);
									}
									/*
									 * conditional date pagination for Request
									 * Parameter
									 */
									if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
									{
										String startDate = null;
										if( retryStartDate == null )
										{
											startDate = convertDateFormat(dateFormat, fromDate, timeZone);
										}
										else
										{
											startDate = fromDate;
										}
										String paginationEndDate = null;
										if( dayRange > 0 )
										{
											paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
										}
										else
										{
											paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
										}
										url += (url.contains("?") ? "&" : "?") + filterName + "=" + fromDateParam + " " + fromDateCondition + " %s " + conditionalParam + " " + toDateParam + " " + toDateCondition + " %s";

										boolean isResultSetCompleted = true;
										if( webServiceApi.getValidateOrPreview() )
										{
											while (isResultSetCompleted)
											{
												getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
												startDate = paginationEndDate;
												if( dayRange > 0 )
												{
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
												}
												else
												{
													paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
												}

												if( compareDates(startDate, convertDateFormat(dateFormat, toDate, timeZone), dateFormat) )
												{
													break;
												}
												else
												{
													if( dataList.size() > 0 )
													{
														break;
													}
													else
													{
														isResultSetCompleted = true;
													}
												}
											}
										}
										else
										{
											UUID uuid = UUID.randomUUID();
											LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
											while (isResultSetCompleted)
											{
												LOG.info("Initiating call for uuid : " + uuid.toString() + " startDate -> " + startDate + " End date -> " + paginationEndDate + " Range -> " + dayRange + " days");
												isResultSetCompleted = getResponseObject(webServiceApi, String.format(url, startDate, paginationEndDate), webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate,
														clientDbDetails);
												LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());

												webServiceApi.setRetryDate(startDate);
												if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
												{
													if( dataList.size() > 0 )
													{
														Connection connection = null;
														try
														{
															connection = getStagingConnection(clientDbDetails);
															System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
															formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
															formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
															System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

															String tableName = webServiceApi.getTable().getTableName();

															if( tableName == null )
															{
																tableName = getTempTableName(formattedApiResponse, connection);
																webServiceApi.getTable().setTableName(tableName);
															}

															List<Column> columns = getTableStructure(tableName, connection);
															Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

															List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
															webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
															writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
															dataList.clear();
														}
														finally
														{
															closeConnection(connection);
														}
													}
													else
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
												{
													if( dataList.size() > 0 )
													{
														FileWriter jsonfileWriter = null;
														try
														{
															System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
															String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
															if( filePath != null )
															{
																jsonfileWriter = new FileWriter(jsonFilePath);
																writeObjectToJson(jsonfileWriter, dataList, false);
															}
															System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
															dataList.clear();
														}
														finally
														{
															closeJsonfileWriter(jsonfileWriter);
														}
													}
												}
												else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
												{
													System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
													writeObjectToXml(filePath, dataList);
													System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
													dataList.clear();
												}
												startDate = paginationEndDate;
												if( dayRange > 0 )
												{
													paginationEndDate = getEndDateByInterval(startDate, dateFormat, dayRange);
												}
												else
												{
													paginationEndDate = convertDateFormat(dateFormat, toDate, timeZone);
												}
												boolean compareDates = compareDates(startDate, convertDateFormat(dateFormat, toDate, timeZone), dateFormat);
												if( compareDates )
												{
													if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
													{
														String tableName = webServiceApi.getTable().getTableName();
														if( tableName == null )
														{
															createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
														}
													}
													break;
												}
												else
												{
													isResultSetCompleted = true;
												}

											}
										}
									}
									else
									{
										/*
										 * conditional date pagination for Body
										 * Parameter
										 */
									}
								}
							}
							else
							{
								JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
								JSONObject paramObject = paramters.getJSONObject(0);
								String paginationNextParam = paramObject.getString("paginationHyperLinkPattern");
								String paginationHypermediaPageLimit = paramObject.getString("paginationHypermediaPageLimit");
								String paginationParamType = paramObject.getString("paginationParamType");
								/* HyperLink pagination for Request Parameter */
								if( paginationParamType.equals(WebServiceConstants.PaginationParamType.REQUEST_PARAMETER.type) )
								{
									boolean isResultSetCompleted = true;

									if( webServiceApi.getValidateOrPreview() )
									{
										getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
									}
									else
									{
										int paginationLimit = -1;
										if( StringUtils.isNotBlank(paginationHypermediaPageLimit) )
										{
											paginationLimit = Integer.parseInt(paginationHypermediaPageLimit);
										}

										List<Object> nextPathList = new ArrayList<>();

										String nextUrlPath = url;

										if( webServiceApi.getRetryPaginationData() != null )
										{
											JSONObject retryPaginationDataObject = new JSONObject(webServiceApi.getRetryPaginationData());
											String retryHyperMediaNextUrl = retryPaginationDataObject.getString("retryHyperMediaNextUrl");
											String retryTempTableName = retryPaginationDataObject.getString("retryTempTableName");
											nextUrlPath = retryHyperMediaNextUrl;
											webServiceApi.getTable().setTableName(retryTempTableName);
										}

										webServiceApi.setRetryHyperMediaNextUrl(nextUrlPath);
										int nextCounter = 0;
										UUID uuid = UUID.randomUUID();
										do
										{
											LOG.info("Initiating call for uuid : " + uuid.toString() + "  nextUrlPath -> " + nextUrlPath);
											isResultSetCompleted = getResponseObject(webServiceApi, nextUrlPath, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, paginationLimit, paginationNextParam,
													nextPathList, clientDbDetails);
											LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
											if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
											{
												if( dataList.size() > 0 )
												{
													Connection connection = null;
													try
													{
														connection = getStagingConnection(clientDbDetails);
														System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
														formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
														formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
														System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

														String tableName = webServiceApi.getTable().getTableName();

														if( tableName == null )
														{
															tableName = getTempTableName(formattedApiResponse, connection);
															webServiceApi.getTable().setTableName(tableName);
														}

														List<Column> columns = getTableStructure(tableName, connection);
														Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

														List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
														webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
														writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
														dataList.clear();
													}
													finally
													{
														closeConnection(connection);
													}
												}
												else
												{
													String tableName = webServiceApi.getTable().getTableName();
													if( tableName == null )
													{
														createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
													}
												}
											}
											else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
											{
												if( dataList.size() > 0 )
												{
													FileWriter jsonfileWriter = null;
													try
													{
														System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
														String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
														if( filePath != null )
														{
															jsonfileWriter = new FileWriter(jsonFilePath);
															writeObjectToJson(jsonfileWriter, dataList, false);
														}
														System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
														dataList.clear();
													}
													finally
													{
														closeJsonfileWriter(jsonfileWriter);
													}
												}
											}
											else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
											{
												System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
												writeObjectToXml(filePath, dataList);
												System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
												dataList.clear();
											}
											LOG.info((++nextCounter) + "; WID:- " + uuid + "; Next URL" + nextPathList);
											if( nextPathList.size() > 0 && (paginationLimit == -1 || (paginationLimit != -1 && isResultSetCompleted)) )
											{

												Object nextUrlPathObject = nextPathList.get(0);
												if( nextUrlPathObject != null && nextUrlPathObject instanceof String && StringUtils.isNotBlank(nextUrlPathObject.toString()) )
												{
													nextUrlPath = nextUrlPathObject.toString();
													nextUrlPath = URLDecoder.decode(nextUrlPath, WebServiceConstants.UTF8_CHARSET);
													LOG.info((nextCounter) + "; WID:- " + uuid + "; Next URL  After Decode " + nextUrlPath);
												}
												else
												{
													nextUrlPath = null;
												}

												nextPathList.clear();
											}
											else
											{
												nextUrlPath = null;
											}
										}
										while (nextUrlPath != null);
									}
								}
							}
						}
						else
						{
							UUID uuid = UUID.randomUUID();
							LOG.info("Retrieval started for the Url : " + url + " with id: " + uuid);
							getResponseObject(webServiceApi, url, webServiceApi.getApiMethodType(), webServiceApi.getResponseObjectName(), postBodyObject, dataList, autheticateParams, null, headers, restTemplate, clientDbDetails);
							LOG.info("Completed call for uuid : " + uuid.toString() + " Records -> " + dataList.size());
							if( !webServiceApi.getValidateOrPreview() )
							{
								if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_CSV) )
								{
									if( dataList.size() > 0 )
									{
										Connection connection = null;
										try
										{
											connection = getStagingConnection(clientDbDetails);
											System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
											formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
											formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
											System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());

											String tableName = webServiceApi.getTable().getTableName();

											if( tableName == null )
											{
												tableName = getTempTableName(formattedApiResponse, connection);
												webServiceApi.getTable().setTableName(tableName);
											}

											List<Column> columns = getTableStructure(tableName, connection);
											Table tempTable = getTempTableAndColumns(webServiceApi.getTable(), columns);

											List<String> alterTableColumns = alterTempTableForWs(formattedApiResponse, tempTable.getOriginalColumnNames(), tableName, connection);
											webServiceApi.getTable().setOriginalColumnNames(alterTableColumns);
											writeDataToTempTable(alterTableColumns, formattedApiResponse, tableName, connection);
											dataList.clear();
										}
										finally
										{
											closeConnection(connection);
										}
									}
									else
									{
										String tableName = webServiceApi.getTable().getTableName();
										if( tableName == null )
										{
											createTempTableUsingMappedHeaders(clientDbDetails, webServiceApi);
										}
									}
								}
								else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_JSON) )
								{
									if( dataList.size() > 0 )
									{
										FileWriter jsonfileWriter = null;
										try
										{
											System.out.println("json file writing Started \t\t\t\t\t\t" + new Date());
											String jsonFilePath = WebServiceJSONWriter.getFilePathForWsApi(webServiceApi.getApiName(), filePath, WebServiceConstants.FILETYPE_JSON);
											if( filePath != null )
											{
												jsonfileWriter = new FileWriter(jsonFilePath);
												writeObjectToJson(jsonfileWriter, dataList, false);
											}
											System.out.println("json file writing Ended \t\t\t\t\t\t" + new Date());
											dataList.clear();
										}
										finally
										{
											closeJsonfileWriter(jsonfileWriter);
										}
									}
								}
								else if( webServiceApi.getWsFileType().equals(WebServiceConstants.FILETYPE_XML) )
								{
									System.out.println("xml file writing Started \t\t\t\t\t\t" + new Date());
									writeObjectToXml(filePath, dataList);
									System.out.println("xml file writing Ended \t\t\t\t\t\t" + new Date());
									dataList.clear();
								}
							}
						}
						if( webServiceApi.getValidateOrPreview() )
						{
							break;
						}
					}
				}
			}
			catch ( Exception e )
			{
				throw new ClientWebserviceRequestException(e.getMessage());
			}
			if( dataList.size() > 0 )
			{
				System.out.println("flatten Started \t\t\t\t\t\t" + new Date());
				if( webServiceApi.getValidateOrPreview() )
				{
					formattedApiResponse = new ApiJsonFlatten(dataList.stream().limit(10).collect(Collectors.toList())).getFlattenJson();
					System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());
				}
				else
				{
					formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
					System.out.println("flatten Ended \t\t\t\t\t\t\t" + new Date());
				}
				formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
				System.out.println("date formatting Ended \t\t\t\t\t\t" + new Date());
			}
		}
		else
		{
			throw new ClientWebserviceRequestException("Authentication failed. Error Code " + authenticationResponse.getStatusCode());
		}

		return formattedApiResponse;
	}

	/**
	 * @param clientDbDetails
	 * @param webServiceApi
	 * @throws Exception
	 */
	private static void createTempTableUsingMappedHeaders(Map<String, Object> clientDbDetails, WebServiceApi webServiceApi) throws Exception
	{
		Connection connection = null;
		try
		{
			connection = getStagingConnection(clientDbDetails);
			Set<String> tempMappingColumns = new HashSet<>();
			String[] map = StringUtils.split(webServiceApi.getMappedHeaders(), "||");
			for (int i = 0; i < map.length; i++)
			{
				String s = map[i];
				String[] iLApiHeaders = s.split("=");
				String apiHeader = iLApiHeaders[1];
				if( !apiHeader.contains("{") )
				{
					tempMappingColumns.add(apiHeader.trim());
				}
			}
			List<String> mappingColumnList = new ArrayList<String>(tempMappingColumns);
			String mappingTableName = WebServiceUtils.getMappingColumnTempTableName(mappingColumnList, connection);
			webServiceApi.getTable().setTableName(mappingTableName);
			webServiceApi.getTable().setOriginalColumnNames(mappingColumnList);
		}
		finally
		{
			closeConnection(connection);
		}

	}

	/**
	 * @param headers
	 * @param flatJson
	 * @param tableName
	 * @param connection
	 * @throws SQLException
	 */
	public static void writeDataToTempTable(List<String> headers, List<LinkedHashMap<String, Object>> flatJson, String tableName, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		try
		{
			int size = flatJson.size();
			Set<String> headersSet = new HashSet<>();
			StringBuilder stringBuilder = new StringBuilder();
			StringJoiner headersData = new StringJoiner(",");
			StringJoiner headersPlaceHolders = new StringJoiner(",");

			statement = connection.createStatement();

			statement.executeQuery("SET NAMES utf8mb4;");
			// statement.execute("ALTER table "+tableName+"
			// ROW_FORMAT=DYNAMIC;");
			stringBuilder.append(" INSERT INTO " + tableName).append(" ( ");
			connection.setAutoCommit(false);
			for (String header : headers)
			{
				if( header.length() >= 65 )
				{
					header = header.substring(0, 64);
				}
				headersSet.add(header);
			}
			for (String header : headersSet)
			{
				headersData.add("`" + header + "`");
				headersPlaceHolders.add("?");
			}
			stringBuilder.append(headersData.toString()).append(" ) ").append(" VALUES ( ").append(headersPlaceHolders.toString()).append(" );");

			preparedStatement = connection.prepareStatement(stringBuilder.toString());
			int counter = 1;
			for (int i = 1; i < size; i++)
			{
				Map<String, Object> map = flatJson.get(i);
				// Modified Column headers
				Map<String, Object> modifiedMap = new HashMap<String, Object>();
				map.forEach((key, value) ->
				{
					if( key.length() >= 65 )
					{
						key = key.substring(0, 64);
					}
					modifiedMap.put(key, value);
				});

				int increment = 1;
				for (String header : headersSet)
				{
					String value = modifiedMap.get(header) == null ? "" : sanitizeCsvForWsTempTable(modifiedMap.get(header).toString());
					preparedStatement.setObject(increment++, value);
				}
				preparedStatement.addBatch();
				increment = 1;
				if( i % 1000 == 0 )
				{
					preparedStatement.executeBatch();
					connection.commit();
					System.out.println("Batch " + (counter++) + " executed successfully.");
				}
			}
			// execute final batch
			preparedStatement.executeBatch();
			connection.commit();
			System.out.println("Batch " + (counter++) + " executed successfully.");
		}
		finally
		{
			if( statement != null )
			{
				statement.close();
			}
			if( preparedStatement != null )
			{
				preparedStatement.close();
			}
		}

	}

	/**
	 * @param clientDbDetails
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	static Connection getStagingConnection(Map<String, Object> clientDbDetails) throws SQLException, ClassNotFoundException
	{
		return getDBConnection(clientDbDetails, true, false);
	}

	/**
	 * @param clientDbDetails
	 * @param isStaging
	 * @param isAppDb
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	static Connection getDBConnection(Map<String, Object> clientDbDetails, boolean isStaging, boolean isAppDb) throws SQLException, ClassNotFoundException
	{
		String schemaName = null;
		if( isStaging ) schemaName = (String) clientDbDetails.get("clientdb_staging_schema");
		else if( isAppDb ) schemaName = (String) clientDbDetails.get("clientappdb_name");
		else schemaName = (String) clientDbDetails.get("clientdb_schema");

		String host = (String) clientDbDetails.get("region_hostname");
		String port = (String) clientDbDetails.get("region_port");
		String user = (String) clientDbDetails.get("clientdb_username");
		String password = (String) clientDbDetails.get("clientdb_password");
		Class.forName(Constants.MYSQL_DRIVER_CLASS);
		return DriverManager.getConnection(Constants.MYSQL_DB_URL + host + ":" + port + "/" + schemaName + "?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&relaxAutoCommit=true&allowMultiQueries=true", user, password);
	}

	/**
	 * @param connection
	 */
	static void closeConnection(Connection connection)
	{
		if( connection != null )
		{
			try
			{
				connection.close();
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param formattedApiResponse
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static String getTempTableName(List<LinkedHashMap<String, Object>> formattedApiResponse, Connection connection) throws Exception
	{

		String tempTableName = null;
		ClientData clientData = new ClientData();
		List<String> headerColumnsFromWs = collectHeaders(formattedApiResponse);
		Table tempTable = tempTableForming(headerColumnsFromWs);
		tempTable.setOriginalColumnNames(headerColumnsFromWs);

		Package userPackage = new Package();

		userPackage.setPackageName("Standard");
		userPackage.setPackageId(0);
		clientData.setUserPackage(userPackage);
		clientData.getUserPackage().setTable(tempTable);

		String query = createTargetTable(clientData, connection);

		if( query != null )
		{
			tempTableName = tempTable.getTableName();
			System.out.println("created temp table name --> " + tempTableName);
		}
		return tempTableName;

	}

	/**
	 * @param headerColumnsFromWs
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static String getMappingColumnTempTableName(List<String> headerColumnsFromWs, Connection connection) throws Exception
	{
		String tempTableName = null;
		ClientData clientData = new ClientData();
		Table tempTable = tempTableForming(headerColumnsFromWs);
		tempTable.setOriginalColumnNames(headerColumnsFromWs);

		Package userPackage = new Package();

		userPackage.setPackageName("Standard");
		userPackage.setPackageId(0);
		clientData.setUserPackage(userPackage);
		clientData.getUserPackage().setTable(tempTable);

		String query = createTargetTable(clientData, connection);

		if( query != null )
		{
			tempTableName = tempTable.getTableName();
			System.out.println("created temp table name --> " + tempTableName);
		}
		return tempTableName;

	}

	/**
	 * @param table
	 * @param columns
	 * @return
	 */
	public static Table getTempTableAndColumns(Table table, List<Column> columns)
	{
		List<String> originaCols = new ArrayList<String>();
		for (Column col : columns)
		{
			originaCols.add(col.getColumnName());
		}
		table.setOriginalColumnNames(originaCols);
		table.setColumns(columns);
		return table;
	}

	/**
	 * @param flatJson
	 * @param columnsList
	 * @param tableName
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public static List<String> alterTempTableForWs(List<LinkedHashMap<String, Object>> flatJson, List<String> columnsList, String tableName, Connection connection) throws SQLException
	{
		PreparedStatement ps = null;
		Set<String> newAndExistingColumns = new HashSet<String>();
		List<String> finalNewColumns = new ArrayList<String>();
		try
		{
			List<String> headerColumnsFromWs = collectHeaders(flatJson);
			newAndExistingColumns.addAll(columnsList);

			Collection<String> similar = new HashSet<String>(headerColumnsFromWs);
			Collection<String> newColumns = new HashSet<String>();
			newColumns.addAll(headerColumnsFromWs);
			newColumns.addAll(columnsList);
			similar.retainAll(columnsList);
			newColumns.removeAll(similar);
			newAndExistingColumns.addAll(newColumns);

			List<String> newColumnsList = new ArrayList<String>();

			for (String columnName : newColumns)
			{
				if( columnName.length() >= 65 )
				{
					columnName = columnName.substring(0, 64);
				}
				newColumnsList.add(columnName);
			}

			for (String columnName : newColumnsList)
			{
				if( !columnsList.contains(columnName) )
				{
					finalNewColumns.add(columnName);
				}
			}

			if( finalNewColumns.size() > 0 )
			{
				StringBuilder alterQuery = new StringBuilder();
				alterQuery.append("ALTER TABLE " + tableName);
				for (String alterColumn : finalNewColumns)
				{
					alterQuery.append(" ADD COLUMN " + alterColumn + " LONGTEXT ").append(",");
				}
				System.out.println("alter query -->" + removeLastChar(alterQuery.toString()) + ";");
				ps = connection.prepareStatement(removeLastChar(alterQuery.toString()) + ";");
				ps.executeUpdate();
			}
		}
		catch ( SQLSyntaxErrorException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( ps != null )
			{
				ps.close();
			}
		}
		return new ArrayList<String>(newAndExistingColumns);
	}

	/**
	 * @param str
	 * @return
	 */
	static String removeLastChar(String str)
	{
		return str.substring(0, str.length() - 1);
	}

	/**
	 * @param tableName
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public static List<Column> getTableStructure(String tableName, Connection connection) throws SQLException
	{
		final String table = tableName;
		List<Column> columnInfo = new ArrayList<>();
		List<Column> primaryKeyColumnInfo = new ArrayList<>();
		ResultSet rs = null;
		ResultSet primaryKeyRs = null;
		try
		{
			java.sql.DatabaseMetaData metaData = connection.getMetaData();
			primaryKeyRs = metaData.getPrimaryKeys(null, null, table);
			rs = metaData.getColumns(null, null, table, null);

			while (primaryKeyRs.next())
			{
				Column primaryKeycolumn = new Column();
				primaryKeycolumn.setColumnName(primaryKeyRs.getString("COLUMN_NAME"));
				primaryKeycolumn.setIsPrimaryKey(primaryKeyRs.getString("PK_NAME").equals("PRIMARY") ? true : false);
				primaryKeyColumnInfo.add(primaryKeycolumn);
			}
			while (rs.next())
			{

				Column column = new Column();
				column.setSchemaName(rs.getString("TABLE_SCHEM"));
				column.setTableName(rs.getString("TABLE_NAME"));
				column.setColumnName(rs.getString("COLUMN_NAME"));
				column.setDataType(rs.getString("TYPE_NAME"));
				if( rs.getString("TYPE_NAME").contains("FLOAT") || rs.getString("TYPE_NAME").contains("DOUBLE") || rs.getString("TYPE_NAME").contains("DECIMAL") || rs.getString("TYPE_NAME").contains("NUMERIC") )
				{
					String scale = rs.getString("DECIMAL_DIGITS");
					if( scale != null )
					{
						column.setColumnSize(rs.getString("COLUMN_SIZE") + "," + rs.getString("DECIMAL_DIGITS"));
					}
					else
					{
						column.setColumnSize(rs.getString("COLUMN_SIZE"));
					}

				}
				else
				{
					column.setColumnSize(rs.getString("COLUMN_SIZE"));
				}
				column.setIsPrimaryKey(false);
				column.setIsNotNull(rs.getString("IS_NULLABLE").equals("YES") ? false : true);
				column.setDefaultValue(rs.getString("COLUMN_DEF"));
				column.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? true : false);
				columnInfo.add(column);
			}
			for (Column pkColumn : primaryKeyColumnInfo)
			{
				for (Column column : columnInfo)
				{
					if( column.getColumnName().equals(pkColumn.getColumnName()) )
					{
						if( pkColumn.getIsPrimaryKey() )
						{
							column.setIsPrimaryKey(true);
						}

					}
				}

			}
			return columnInfo;

		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getTableStructure()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		finally
		{
			if( rs != null )
			{
				rs.close();
			}
			if( primaryKeyRs != null )
			{
				primaryKeyRs.close();
			}
		}
	}

	/**
	 * @param flatJson
	 * @return
	 */
	public static List<LinkedHashMap<String, Object>> getResultsFromApiResponse(List<LinkedHashMap<String, Object>> flatJson)
	{

		List<LinkedHashMap<String, Object>> formattedApiResponse = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		LinkedHashMap<String, Object> mainHeaders = new LinkedHashMap<String, Object>();
		for (LinkedHashMap<String, Object> h : flatJson)
		{
			h.forEach((k, v) ->
			{
				if( !headers.contains(k) )
				{
					headers.add(k);
					mainHeaders.put(k, "");
				}
			});
		}
		formattedApiResponse.add(mainHeaders);
		int headersSize = headers.size();

		for (LinkedHashMap<String, Object> data1 : flatJson)
		{
			LinkedHashMap<String, Object> currentData = new LinkedHashMap<String, Object>();
			Object currentValue = null;
			int i = 0;
			do
			{
				String mainHeader = headers.get(i);
				for (Map.Entry<String, Object> entry : data1.entrySet())
				{

					String key = entry.getKey();
					Object value = entry.getValue();
					if( value != null && value.toString().contains("/Date(") )
					{
						String date = value.toString();
						date = date.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
						Long dateInMilliseconds = Long.parseLong(date);
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String s = f.format(new Date(dateInMilliseconds));
						value = s;
					}
					if( mainHeader.equals(key) )
					{
						currentValue = value;
					}

				}
				i++;
				currentData.put(mainHeader, currentValue);
				currentValue = "";
			}
			while (i < headersSize);

			formattedApiResponse.add(currentData);
		}

		return formattedApiResponse;
	}

	/**
	 * @param webServiceApi
	 * @param apiUrl
	 * @param methodType
	 * @param responseObjectName
	 * @param postBodyObject
	 * @param dataList
	 * @param autheticateParams
	 * @param headerParams
	 * @param headers
	 * @param restTemplate
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static boolean getResponseObject(WebServiceApi webServiceApi, String apiUrl, String methodType, String responseObjectName, JSONObject postBodyObject, List<Object> dataList, Map autheticateParams, HttpEntity<String> headerParams, HttpHeaders headers, RestTemplate restTemplate,
			Map<String, Object> clientDbDetails) throws Exception
	{
		return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, headerParams, headers, restTemplate, -1, clientDbDetails);
	}

	/**
	 * @param webServiceApi
	 * @param apiUrl
	 * @param methodType
	 * @param responseObjectName
	 * @param postBodyObject
	 * @param dataList
	 * @param autheticateParams
	 * @param headerParams
	 * @param headers
	 * @param restTemplate
	 * @param limit
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static boolean getResponseObject(WebServiceApi webServiceApi, String apiUrl, String methodType, String responseObjectName, JSONObject postBodyObject, List<Object> dataList, Map autheticateParams, HttpEntity<String> headerParams, HttpHeaders headers, RestTemplate restTemplate, int limit,
			Map<String, Object> clientDbDetails) throws Exception
	{
		return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, headerParams, headers, restTemplate, limit, null, null, clientDbDetails);
	}

	/**
	 * @param webServiceApi
	 * @param apiUrl
	 * @param methodType
	 * @param responseObjectName
	 * @param postBodyObject
	 * @param dataList
	 * @param autheticateParams
	 * @param headerParams
	 * @param headers
	 * @param restTemplate
	 * @param limit
	 * @param nextLinkUrlPattern
	 * @param nextLinkList
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static boolean getResponseObject(WebServiceApi webServiceApi, String apiUrl, String methodType, String responseObjectName, JSONObject postBodyObject, List<Object> dataList, Map autheticateParams, HttpEntity<String> headerParams, HttpHeaders headers, RestTemplate restTemplate, int limit,
			String nextLinkUrlPattern, List<Object> nextLinkList, Map<String, Object> clientDbDetails) throws Exception
	{
		return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, headerParams, headers, restTemplate, limit, 0, nextLinkUrlPattern, nextLinkList, clientDbDetails);
	}

	/**
	 * @param webServiceApi
	 * @param apiUrl
	 * @param methodType
	 * @param responseObjectName
	 * @param postBodyObject
	 * @param dataList
	 * @param autheticateParams
	 * @param headerParams
	 * @param headers
	 * @param restTemplate
	 * @param limit
	 * @param retryCount
	 * @param nextLinkUrlPattern
	 * @param nextLinkList
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean getResponseObject(WebServiceApi webServiceApi, String apiUrl, String methodType, String responseObjectName, JSONObject postBodyObject, List<Object> dataList, Map autheticateParams, HttpEntity<String> headerParams, HttpHeaders headers, RestTemplate restTemplate, int limit,
			int retryCount, String nextLinkUrlPattern, List<Object> nextLinkList, Map<String, Object> clientDbDetails) throws Exception
	{
		ResponseEntity<?> restApiResponse = null;
		int initialDataListSize = dataList.size();
		long webserviceTypeId = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId();

	 /*  if( webserviceTypeId == WebServiceConstants.AuthenticationType.OAUTH1.type )
		{
			String requiredRequestParams = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthRequestParams();
			if( StringUtils.isNotBlank(requiredRequestParams) )
			{
				JSONObject requiredRequestParamsJObj = new JSONObject(requiredRequestParams);
				if( requiredRequestParamsJObj.length() > 0 )
				{
					String oAuth1AuthenticateParams = getOAuth1AuthenticateParams(webServiceApi, apiUrl, methodType);
					if( oAuth1AuthenticateParams.toString().trim().length() > 0 )
					{
						apiUrl += (apiUrl.contains("?") ? "&" : "?") + oAuth1AuthenticateParams;
					}
				}
			}

			String requiredBodyParams = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthBodyParams();
			if( StringUtils.isNotBlank(requiredBodyParams) )
			{
				JSONObject requiredBodyParamsJObj = new JSONObject(requiredBodyParams);
				if( requiredBodyParamsJObj.length() > 0 )
				{
					addOAuth1BodyAuthenticateParams(webServiceApi, apiUrl, methodType, postBodyObject);
				}
			}

			String requiredRequestHeaders = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthRequestHeaders();

			if( StringUtils.isNotBlank(requiredRequestHeaders) )
			{
				addOAuth1HeaderAuthenticateParams(webServiceApi, apiUrl, methodType, headers);
			}
		} */

		URI uri = UriComponentsBuilder.fromUriString(apiUrl).build().encode().toUri();
		int retryLimit = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getRetryLimit();
		int wsSleepTIme = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getSleepTime();

		int diffSize = 0;
		try
		{
			if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebserviceType().equals(WebServiceConstants.WebserviceType.SOAP.type) )
			{
				if( methodType.equals(WebServiceConstants.HTTP_METHOD_GET) )
				{
					headerParams = new HttpEntity<>(headers);
					restApiResponse = restTemplate.exchange(uri, HttpMethod.GET, headerParams, String.class);
					LOG.debug("calling API Started ");
				}
				else if( methodType.equals(WebServiceConstants.HTTP_METHOD_POST) )
				{
					HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(webServiceApi.getSoapBodyElement(), headers);
					restApiResponse = restTemplate.exchange(uri, HttpMethod.POST, headerParamsPost, String.class);
					LOG.debug("calling API Ended ");
				}
				if( responseObjectName != null && restApiResponse != null && restApiResponse.getStatusCode().equals(HttpStatus.OK) )
				{
					String response = (String) restApiResponse.getBody();
					getSoapObject(response, responseObjectName, dataList);
				}
			}
			else
			{
				if( methodType.equals(WebServiceConstants.HTTP_METHOD_GET) )
				{
					if( webserviceTypeId != WebServiceConstants.AuthenticationType.OAUTH1.type )
					{
						headerParams = new HttpEntity<>(headers);
						restApiResponse = restTemplate.exchange(uri, HttpMethod.GET, headerParams, Object.class);
					}
					else
					{
						Response response = getOAuth1Response(  apiUrl,  webServiceApi);
						if(response.getCode() == 200){
							ObjectMapper mapper = new ObjectMapper();
					        Object object = mapper.readValue(response.getBody(), Object.class);
							restApiResponse = ResponseEntity.ok().body(object);
						}else{
							throw new ClientWebserviceRequestException(response.getCode()+" "+response.getMessage());
						}

					}
					LOG.debug("calling API Started ");
				}
				else if( methodType.equals(WebServiceConstants.HTTP_METHOD_POST) )
				{
					if( webserviceTypeId != WebServiceConstants.AuthenticationType.OAUTH1.type )
					{
						HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(postBodyObject.toString(), headers);
						restApiResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, headerParamsPost, Object.class);
					}
					else
					{
						Response response = postOAuth1Response(  uri.toString(),  webServiceApi,postBodyObject);
						if(response.getCode() == 200){
							ObjectMapper mapper = new ObjectMapper();
					        Object object = mapper.readValue(response.getBody(), Object.class);
							restApiResponse = ResponseEntity.ok().body(object);
						}else{
							throw new ClientWebserviceRequestException(response.getCode()+" "+response.getMessage());
						}
					}
					LOG.debug("calling API Ended ");
				}
				if( responseObjectName != null && restApiResponse != null && restApiResponse.getStatusCode().equals(HttpStatus.OK) )
				{
					if( restApiResponse.getBody() != null )
					{
						if( restApiResponse.getBody() instanceof Map )
						{
							Map<String, Object> sourceObject = (Map<String, Object>) restApiResponse.getBody();
							if( StringUtils.isNotBlank(responseObjectName) )
							{
								String[] patternSplit = StringUtils.split(responseObjectName, ".");
								getObjects(patternSplit, 0, sourceObject, dataList);

								if( StringUtils.isNotBlank(nextLinkUrlPattern) )
								{
									String[] nextUrlLinkPatternSplit = StringUtils.split(nextLinkUrlPattern, ".");
									getObjects(nextUrlLinkPatternSplit, 0, sourceObject, nextLinkList);
								}
							}
							else
							{
								dataList.add((Map) restApiResponse.getBody());
							}
						}
						else if( restApiResponse.getBody() instanceof List )
						{
							JSONObject jSONObject = new JSONObject(webServiceApi.getApiPathParams());
							if( jSONObject.length() > 0 )
							{
								if( StringUtils.isNotBlank(responseObjectName) )
								{
									List<Map<String, Object>> list = (List<Map<String, Object>>) restApiResponse.getBody();

									for (Map<String, Object> sourceObject : list)
									{
										String[] patternSplit = StringUtils.split(responseObjectName, ".");
										getObjects(patternSplit, 0, sourceObject, dataList);

										if( StringUtils.isNotBlank(nextLinkUrlPattern) )
										{
											String[] nextUrlLinkPatternSplit = StringUtils.split(nextLinkUrlPattern, ".");
											getObjects(nextUrlLinkPatternSplit, 0, sourceObject, nextLinkList);
										}
									}
								}
								else
								{
									dataList.addAll((List) restApiResponse.getBody());
								}
							}
							else
							{
								dataList.addAll((List) restApiResponse.getBody());
							}
						}
					}
				}
			}
			int finalDataListSize = dataList.size();
			diffSize = finalDataListSize - initialDataListSize;
			LOG.debug("fetched Data List Size:" + finalDataListSize + " difference with previous fetch :" + diffSize);

			if( limit != -1 && limit != diffSize )
			{
				LOG.debug("Terminating the data fetching\nFetched Data (" + diffSize + ") and limit (" + limit + ") not matching for " + apiUrl);
				diffSize = 0;
			}
		}
		catch ( HttpMessageNotReadableException | SocketException | ConnectionClosedException se )
		{
			LOG.debug(" HttpMessageNotReadableException | SocketException | ConnectionClosedException  url:" + " -- " + apiUrl);
			retryCount++;
			try
			{
				int sleepTime = (int) Math.pow(3, retryCount - 1) * wsSleepTIme;
				LOG.debug("HttpMessageNotReadableException | SocketException | ConnectionClosedException seconds" + " -- " + sleepTime);
				Thread.sleep(sleepTime);
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
				throw new ClientWebserviceRequestException("Sleep timer omitted " + e.getMessage());
			}
			if( retryCount <= retryLimit )
			{
				return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, retryCount, nextLinkUrlPattern, nextLinkList, clientDbDetails);
			}
			else
			{
				//webServiceApi.setRetryCount(retryCount);
				throw new ClientWebserviceRequestException(retryLimit + " retries completed for SocketException");
			}
		}
		catch ( AnvizentRuntimeException he )
		{
			LOG.debug("Too many Requests from handling AnvizentRuntimeException ");
			String statusCode = he.getMessage();
			if( statusCode.equals(WebServiceConstants.HTTP_TOO_MANY_REQUESTS) || statusCode.equals(WebServiceConstants.HTTP_REQUEST_TIMEOUT) || statusCode.equals(WebServiceConstants.HTTP_INTERNAL_SERVER_ERROR) || statusCode.equals(WebServiceConstants.HTTP_SERVICE_UNAVAILABLE)
					|| statusCode.equals(WebServiceConstants.HTTP_FORBIDDEN) || statusCode.equals(WebServiceConstants.HTTP_NOT_FOUND) || statusCode.equals(WebServiceConstants.HTTP_UNAUTHORIZED) )
			{
				LOG.debug("Too many Requests Api url:" + " -- " + apiUrl);
			  	retryCount++;

				try
				{
					int sleepTime = (int) Math.pow(3, retryCount - 1) * wsSleepTIme;
					LOG.debug("Too many Requests Retry seconds" + " -- " + sleepTime);
					Thread.sleep(sleepTime);
				}
				catch ( InterruptedException e )
				{
					throw new ClientWebserviceRequestException("Sleep timer omitted " + e.getMessage());
				} 

				if( webserviceTypeId == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type )
				{
					if( statusCode.equals(HttpStatus.UNAUTHORIZED) )
					{
						closeAPISession(webServiceApi.getWebServiceConnectionMaster(), restTemplate);
						testAuthenticationUrl(webServiceApi.getWebServiceConnectionMaster(), restTemplate, clientDbDetails);
					}
				}
				if( webserviceTypeId == WebServiceConstants.AuthenticationType.OAUTH2.type )
				{
					if( statusCode.equals(HttpStatus.UNAUTHORIZED) )
					{
						refreshOauth2Token(headers, webServiceApi, restTemplate, clientDbDetails);
					}
				}
			   if( retryCount <= retryLimit )
				{
					return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, retryCount, nextLinkUrlPattern, nextLinkList, clientDbDetails);
				}
				else
				{
					//webServiceApi.setRetryCount(retryCount);
					throw new ClientWebserviceRequestException("Status code :" + statusCode + " " + retryLimit + " retries completed for too many requests");
				} 
			}
			else
			{
				LOG.debug("Error occured while fetching data");
				throw new ClientWebserviceRequestException(he);
			}

		}
		catch ( Exception he )
		{
			LOG.debug("Too many Requests from handling Exception ");
			if( he instanceof HttpServerErrorException )
			{
				throw new ClientWebserviceRequestException(((HttpServerErrorException) he).getResponseBodyAsString(), he);
			}
			else if( he instanceof HttpClientErrorException )
			{
				HttpClientErrorException clientEx = ((HttpClientErrorException) he);
				HttpStatus statusCode = clientEx.getStatusCode();
				if( statusCode.equals(HttpStatus.TOO_MANY_REQUESTS) || statusCode.equals(HttpStatus.REQUEST_TIMEOUT) || statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR) || statusCode.equals(HttpStatus.SERVICE_UNAVAILABLE) || statusCode.equals(HttpStatus.FORBIDDEN)
						|| statusCode.equals(HttpStatus.UNAUTHORIZED) || statusCode.equals(HttpStatus.NOT_FOUND))
				{
					LOG.debug("Too many Requests Api url:" + " -- " + apiUrl);
					retryCount++;

					try
					{
						int sleepTime = (int) Math.pow(3, retryCount - 1) * wsSleepTIme;
						LOG.debug("Too many Requests Retry seconds" + " -- " + sleepTime);
						Thread.sleep(sleepTime);
					}
					catch ( InterruptedException e )
					{
						throw new ClientWebserviceRequestException("Sleep timer omitted " + e.getMessage());
					} 
					if( webserviceTypeId == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type )
					{
						if( statusCode.equals(HttpStatus.UNAUTHORIZED) )
						{
							closeAPISession(webServiceApi.getWebServiceConnectionMaster(), restTemplate);
							testAuthenticationUrl(webServiceApi.getWebServiceConnectionMaster(), restTemplate, clientDbDetails);
						}
					}
					if( webserviceTypeId == WebServiceConstants.AuthenticationType.OAUTH2.type )
					{
						if( statusCode.equals(HttpStatus.UNAUTHORIZED) )
						{
							refreshOauth2Token(headers, webServiceApi, restTemplate, clientDbDetails);
						}
					}
					if( retryCount <= retryLimit )
					{
						return getResponseObject(webServiceApi, apiUrl, methodType, responseObjectName, postBodyObject, dataList, autheticateParams, null, headers, restTemplate, limit, retryCount, nextLinkUrlPattern, nextLinkList, clientDbDetails);
					}
					else
					{
						//webServiceApi.setRetryCount(retryCount);
						throw new ClientWebserviceRequestException("Status code :" + statusCode + " " + retryLimit + " retries completed for too many requests");
					} 
				}else{
					LOG.debug("Error occured while fetching data");
					throw new ClientWebserviceRequestException(he);
				}
			}
			else
			{
				LOG.debug("Error occured while fetching data");
				throw new ClientWebserviceRequestException(he);
			}
		}

		return diffSize == 0 ? false : true;
	}

	/**
	 * @param patternSplit
	 * @param patternIndex
	 * @param sourceObject
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	static void getObjects(String[] patternSplit, int patternIndex, Map<String, Object> sourceObject, List<Object> response)
	{

		if( sourceObject != null && patternIndex <= patternSplit.length && patternSplit.length > 0 )
		{

			String currentPattern = patternSplit[patternIndex];
			if( currentPattern.endsWith("{}") )
			{
				Map<String, Object> resp = (Map<String, Object>) sourceObject.get(currentPattern.substring(0, currentPattern.length() - 2));
				getObjects(patternSplit, patternIndex + 1, resp, response);
			}
			else if( currentPattern.endsWith("[]") )
			{
				List<Object> resp = (List<Object>) sourceObject.get(currentPattern.substring(0, currentPattern.length() - 2));
				if( resp != null )
				{
					int dataLength = resp.size();
					for (int i = 0; i < dataLength; i++)
					{
						Map<String, Object> intResp = (Map<String, Object>) resp.get(i);
						getObjects(patternSplit, patternIndex + 1, intResp, response);
					}
				}
			}
			else
			{
				Object finalOutput = sourceObject.get(currentPattern.substring(0, currentPattern.length()));

				if( finalOutput != null )
				{
					if( finalOutput instanceof List )
					{
						List<Object> finalArray = (List<Object>) finalOutput;
						for (Object arr : finalArray)
						{
							response.add(arr);
						}
					}
					else
					{
						response.add(finalOutput);
					}

				}

			}
		}
	}

	/**
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static ArrayList<String> getParameterVariables(String source, String pattern)
	{
		ArrayList<String> variableNames = new ArrayList<>();

		int lastIndex = 0;
		while (source.indexOf(pattern, lastIndex) != -1)
		{
			String param = "";
			int startIndex = source.indexOf(pattern, lastIndex);
			int endIndex = source.indexOf("}", startIndex);
			param = source.substring(startIndex + 2, endIndex);

			if( variableNames.indexOf(param) == -1 )
			{
				variableNames.add(param);
			}

			lastIndex = endIndex;
		}

		return variableNames;
	}

	/**
	 * @param requiredDateFormat
	 * @param date
	 * @param timeZone
	 * @return
	 * @throws Exception
	 * @throws java.text.ParseException
	 */
	static String convertDateFormat(String requiredDateFormat, String date, String timeZone) throws Exception, java.text.ParseException
	{

		int dayOfYear = LocalDate.parse(date).getDayOfYear();
		int year = LocalDate.parse(date).getYear();

		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		c.set(1, 0, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, dayOfYear);

		SimpleDateFormat sdf = new SimpleDateFormat(requiredDateFormat);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

		return sdf.format(c.getTime()).toString();
	}

	/**
	 * @param startDate
	 * @param todayDate
	 * @param dateFormat
	 * @return
	 * @throws java.text.ParseException
	 */
	static boolean compareDates(String startDate, String todayDate, String dateFormat) throws java.text.ParseException
	{
		boolean status = false;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date1 = sdf.parse(startDate);
		Date date2 = sdf.parse(todayDate);
		if( date1.compareTo(date2) >= 0 )
		{
			status = true;
		}
		return status;
	}

	/**
	 * @param fromDate
	 * @param dateFormat
	 * @param interval
	 * @return
	 * @throws java.text.ParseException
	 */
	static String getEndDateByInterval(String fromDate, String dateFormat, int interval) throws java.text.ParseException
	{
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		Date startDate = (Date) formatter.parse(fromDate);

		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, interval);
		Date calculatedDateAndTime = c.getTime();

		return formatter.format(calculatedDateAndTime);

	}

	/**
	 * @param headers
	 * @param webServiceApi
	 * @param restTemplate
	 * @param clientDbDetails
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	static boolean refreshOauth2Token(HttpHeaders headers, WebServiceApi webServiceApi, RestTemplate restTemplate, Map<String, Object> clientDbDetails) throws Exception
	{

		boolean isTokenRefreshed = false;
		Long authenticationType = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId();
		if( authenticationType == WebServiceConstants.AuthenticationType.OAUTH2.type )
		{
			ResponseEntity<Map> authenticationResponse = null;
			authenticationResponse = testAuthenticationUrl(webServiceApi.getWebServiceConnectionMaster(), restTemplate, clientDbDetails);

			if( authenticationResponse.getStatusCode().equals(HttpStatus.OK) || authenticationResponse.getStatusCode().equals(HttpStatus.NO_CONTENT) )
			{
				Map autheticateParams = null;
				if( authenticationResponse.getBody() != null && authenticationResponse.getBody() instanceof Map )
				{
					autheticateParams = authenticationResponse.getBody();
				}

				String requiredRequestHeaders = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getApiAuthRequestHeaders();

				if( requiredRequestHeaders != null && autheticateParams != null )
				{

					ArrayList<String> autheticateParamVariables = getParameterVariables(requiredRequestHeaders, "{$");
					for (String variable : autheticateParamVariables)
					{
						if( autheticateParams.get(variable) != null )
						{
							requiredRequestHeaders = StringUtils.replace(requiredRequestHeaders, "{$" + variable + "}", autheticateParams.get(variable).toString());
						}
						else
						{
							throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
						}
					}

					ArrayList<String> autheticateHeaderVariables = getParameterVariables(requiredRequestHeaders, "{#");

					if( autheticateHeaderVariables.size() > 0 )
					{
						String headerKeyvalues = webServiceApi.getWebServiceConnectionMaster().getHeaderKeyvalues();
						if( headerKeyvalues != null )
						{
							JSONObject headerKeyvaluesObj = new JSONObject(headerKeyvalues);
							for (String variable : autheticateHeaderVariables)
							{
								if( headerKeyvaluesObj.get(variable) != null )
								{
									requiredRequestHeaders = StringUtils.replace(requiredRequestHeaders, "{#" + variable + "}", headerKeyvaluesObj.get(variable).toString());
								}
								else
								{
									throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
								}
							}
						}
						else
						{
							throw new ClientWebserviceRequestException("required header parameters not found in authentication response");
						}
					}

					String[] rHeaders = StringUtils.split(requiredRequestHeaders, ";");

					for (String hdr : rHeaders)
					{
						String[] eachHeader = StringUtils.split(hdr, ":");

						if( headers.containsKey(eachHeader[0]) )
						{
							headers.remove(eachHeader[0]);
						}
						headers.add(eachHeader[0], eachHeader[1]);
					}
					isTokenRefreshed = true;

				}

			}
		}

		return isTokenRefreshed;
	}

	/**
	 * @param restTemplate
	 * @param webServiceConnectionMaster
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseEntity testAuthenticationUrl(RestTemplate restTemplate, WebServiceConnectionMaster webServiceConnectionMaster)
	{

		String soapBodyElement = null;
		ResponseEntity<String> response = null;
		String authenticationUrlMethodType = null;
		JSONObject authBodyParamJsonObj = null;
		try
		{
			String authBodyParams = webServiceConnectionMaster.getBodyParams();
			JSONObject bodyParams = new JSONObject();
			if( StringUtils.isNotBlank(authBodyParams) )
			{
				authBodyParamJsonObj = new JSONObject(authBodyParams);
				Iterator<String> keys = authBodyParamJsonObj.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					bodyParams.put(key, authBodyParamJsonObj.getString(key));
				}
			}

			soapBodyElement = webServiceConnectionMaster.getWebServiceTemplateMaster().getSoapBodyElement();
			String soapBodyParams = webServiceConnectionMaster.getBodyParams();
			if( soapBodyParams != null )
			{
				JSONObject soapBodyParamsJsonObj = new JSONObject(soapBodyParams);
				Iterator<String> keys = soapBodyParamsJsonObj.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					soapBodyElement = StringUtils.replace(soapBodyElement, "{$" + key + "}", soapBodyParamsJsonObj.getString(key));
				}
			}

			authenticationUrlMethodType = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationMethodType();

			if( authenticationUrlMethodType.equalsIgnoreCase("post") )
			{
				HttpHeaders soapHeader = new HttpHeaders();
				soapHeader.add("Content-Type", "text/plain");
				soapHeader.add("SOAPAction", "search");
				HttpEntity<String> entity = new HttpEntity<>(soapBodyElement, soapHeader);
				String endPointUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl();

				try
				{
					response = restTemplate.exchange(endPointUrl, HttpMethod.POST, entity, String.class);
				}
				catch ( Exception exception )
				{
					response = new ResponseEntity(null, HttpStatus.BAD_REQUEST);
					exception.printStackTrace();
				}

			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return response;

	}

	/**
	 * @param currentPath
	 * @param currentNode
	 * @param nodemap
	 * @return
	 */
	private static Map<String, String> flattXml(String currentPath, Node currentNode, Map<String, String> nodemap)
	{
		if( currentNode.getNodeType() == Node.TEXT_NODE && !currentNode.getNodeValue().trim().isEmpty() )
		{
			nodemap.put(currentPath.replaceAll("\\s+", "_").replaceAll("\\W+", "_"), currentNode.getNodeValue());
		}
		else
		{
			NodeList childNodes = currentNode.getChildNodes();
			int length = childNodes.getLength();
			String nextPath = currentPath.isEmpty() ? currentNode.getNodeName() : currentPath + "_" + currentNode.getNodeName().substring(currentNode.getNodeName().indexOf(":"));
			for (int i = 0; i < length; i++)
			{
				Node item = childNodes.item(i);
				flattXml(nextPath, item, nodemap);
			}
		}
		return nodemap;
	}

	/**
	 * @param response
	 * @param reqResponseObj
	 * @param list
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void getSoapObject(String response, String reqResponseObj, List<Object> list) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(response)));
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement();
		NodeList nodeList = root.getElementsByTagName(reqResponseObj);

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Map<String, String> nodemap = new LinkedHashMap<>();
			Node item = nodeList.item(i);
			Map<String, String> node = flattXml("", item, nodemap);
			list.add(node);

		}
	}

	/**
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static RestTemplate getRestTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		TrustStrategy acceptingTrustStrategy = new TrustStrategy()
		{
			@Override
			public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
			{
				return true;
			}
		};
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		restTemplate.setErrorHandler(new MyResponseErrorHandler());
		return restTemplate;
	}

	/**
	 * @return
	 */
	public static MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter()
	{
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
		return mappingJackson2HttpMessageConverter;
	}

	/**
	 * @param webServiceConnectionMaster
	 * @param restTemplate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ResponseEntity<Map> closeAPISession(WebServiceConnectionMaster webServiceConnectionMaster, RestTemplate restTemplate)
	{
		JSONObject sessionAuthUrlObj = null;
		String authenticationUrlMethodType = null;
		String logoutUrl = null;
		ResponseEntity<Map> response = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			headers.add("Accept", "application/json");

			Long authenticationType = webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId();
			authenticationUrlMethodType = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationMethodType();

			if( authenticationType == WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type )
			{
				if( StringUtils.isNotEmpty(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiSessionAuthURL()) )
				{
					sessionAuthUrlObj = new JSONObject(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiSessionAuthURL());
				}
				if( sessionAuthUrlObj.length() != 0 )
				{
					if( webServiceConnectionMaster.getWebServiceTemplateMaster().isBaseUrlRequired() ) logoutUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl() + sessionAuthUrlObj.getString("logoutUrl");
					else logoutUrl = sessionAuthUrlObj.getString("logoutUrl");
				}

				URI logoutUri = UriComponentsBuilder.fromHttpUrl(logoutUrl).build().encode().toUri();
				HttpEntity<Object> httpEntity = new HttpEntity<>(sessionCookieHeaders);
				if( authenticationUrlMethodType.equalsIgnoreCase("post") )
				{
					try
					{
						response = restTemplate.postForEntity(logoutUri, httpEntity, Map.class);
					}
					catch ( Exception exception )
					{
						response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
					}
				}
				else if( authenticationUrlMethodType.equalsIgnoreCase("get") )
				{
					try
					{
						response = restTemplate.getForEntity(logoutUri, Map.class);
					}
					catch ( Exception exception )
					{
						response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
					}
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			sessionCookieHeaders = null;
		}
		return response;
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static String getJSONObjectKey(JSONObject jsonObject, String key)
	{
		String param = null;
		if( jsonObject != null && jsonObject.has(key) )
		{
			if( key.equals("retryDatePage") )
			{
				param = String.valueOf(jsonObject.getInt(key));
			}
			else if( key.equals("retryIncrementalDatePage") )
			{
				param = String.valueOf(jsonObject.getInt(key));
			}
			else
			{
				param = jsonObject.getString(key);
			}
		}
		return param;
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static boolean getJSONObjectKeyAsBoolean(JSONObject jsonObject, String key)
	{
		boolean param = false;
		if( jsonObject != null && jsonObject.has(key) )
		{
			param = jsonObject.getBoolean(key);
		}
		return param;
	}

	/**
	 * @return
	 */
	public static String getToDate()
	{
		String toDate = new SimpleDateFormat(WebServiceConstants.TODAY_DATE_FORMAT).format(new Date());
		return toDate;
	}

	public static Map<String, String> getOAuth1AccessToken(WebServiceConnectionMaster webServiceConnectionMaster) throws Exception
	{
		Map<String, String> resp = new HashMap<>();
		String requestUrl = null;
		String tokenURL = null;
		String authURL = null;
		Token accessToken = null;
		WebServiceTemplateMaster webServiceTemplateMaster = webServiceConnectionMaster.getWebServiceTemplateMaster();

		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		if( StringUtils.isBlank(oAuth1.getToken()) && StringUtils.isBlank(oAuth1.getTokenSecret()) )
		{
			if( webServiceTemplateMaster.isBaseUrlRequired() )
			{
				requestUrl = webServiceTemplateMaster.getBaseUrl() + oAuth1.getRequestURL();
				tokenURL = webServiceTemplateMaster.getBaseUrl() + oAuth1.getTokenURL();
				authURL = webServiceTemplateMaster.getBaseUrl() + oAuth1.getAuthURL() + "?oauth_token=";
			}
			else
			{
				requestUrl = oAuth1.getRequestURL();
				tokenURL = oAuth1.getTokenURL();
				authURL = oAuth1.getAuthURL();
			}
			MagentoThreeLeggedOAuth1 MagentoThreeLeggedOAuth1 = new MagentoThreeLeggedOAuth1(requestUrl, tokenURL, authURL);
			MagentoServiceOAuth1 magentoServiceOAuth1 = new MagentoServiceOAuth1(oAuth1.getConsumerKey(), oAuth1.getConsumerSecret(), oAuth1.getCallbackURL(),oAuth1.getScope(), MagentoThreeLeggedOAuth1);
			OAuthService oAuthService = magentoServiceOAuth1.getOAuthService();

			Token requestToken = new Token(oAuth1.getRequestToken(), oAuth1.getRequestTokenSecret());

			accessToken = magentoServiceOAuth1.getAccessToken(oAuthService, requestToken, oAuth1.getVerifier());

			resp.put("oauth_token", accessToken.getToken());
			resp.put("oauth_token_secret", accessToken.getSecret());
		}
		else
		{
			resp.put("oauth_token", oAuth1.getToken());
			resp.put("oauth_token_secret", oAuth1.getTokenSecret());
		}

		return resp;

	}

	public static String getOAuth1AuthenticateParams(WebServiceApi webServiceApi, String apiUrl, String methodType) throws Exception
	{
		StringJoiner oAuth1RequestParams = new StringJoiner("&");

		WebServiceTemplateMaster webServiceTemplateMaster = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster();

		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		Long seconds = (System.currentTimeMillis() / 1000);
		String oauthTimestamp = seconds.toString();
		String nonce = getAlphaNumericString(6);

		oAuth1RequestParams.add("oauth_consumer_key=" + oAuth1.getConsumerKey());
		oAuth1RequestParams.add("oauth_nonce=" + nonce);
		oAuth1RequestParams.add("oauth_signature_method=" + oAuth1.getSignatureMethod());
		oAuth1RequestParams.add("oauth_version=" + oAuth1.getVersion());

		String key = getkey(oAuth1.getTokenSecret(), oAuth1.getConsumerSecret());
		String data = getOAuthSignature(methodType, apiUrl, oAuth1.getToken(), oAuth1.getConsumerKey(), oAuth1.getSignatureMethod(), oauthTimestamp, nonce, oAuth1.getVersion());
		String signature = HmacSha1Signature.calculateRFC2104HMAC(data, key);

		oAuth1RequestParams.add("oauth_signature=" + signature);
		oAuth1RequestParams.add("oauth_timestamp=" + oauthTimestamp);
		if( !apiUrl.contains("oauth_token") ) oAuth1RequestParams.add("oauth_token=" + oAuth1.getToken());

		return oAuth1RequestParams.toString();
	}

	public static void addOAuth1BodyAuthenticateParams(WebServiceApi webServiceApi, String apiUrl, String methodType, JSONObject postBodyObject) throws Exception
	{

		WebServiceTemplateMaster webServiceTemplateMaster = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster();

		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		Long seconds = (System.currentTimeMillis() / 1000);
		String oauthTimestamp = seconds.toString();
		String nonce = getAlphaNumericString(6);

		postBodyObject.put("oauth_consumer_key", oAuth1.getConsumerKey());
		postBodyObject.put("oauth_nonce", nonce);
		postBodyObject.put("oauth_signature_method", oAuth1.getSignatureMethod());
		postBodyObject.put("oauth_version", oAuth1.getVersion());

		String key = getkey(oAuth1.getTokenSecret(), oAuth1.getConsumerSecret());
		String data = getOAuthSignature(methodType, apiUrl, oAuth1.getToken(), oAuth1.getConsumerKey(), oAuth1.getSignatureMethod(), oauthTimestamp, nonce, oAuth1.getVersion());
		String signature = HmacSha1Signature.calculateRFC2104HMAC(data, key);

		postBodyObject.put("oauth_signature", signature);
		postBodyObject.put("oauth_timestamp", oauthTimestamp);
		if( !postBodyObject.has("oauth_token") ) postBodyObject.put("oauth_token", oAuth1.getToken());

	}

	public static void addOAuth1HeaderAuthenticateParams(WebServiceApi webServiceApi, String apiUrl, String methodType, HttpHeaders headers) throws Exception
	{

		// List<String> headerList = headers.get("Authorization");

		StringJoiner oAuth1RequestParams = new StringJoiner(",");

		WebServiceTemplateMaster webServiceTemplateMaster = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster();

		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		Long seconds = (System.currentTimeMillis() / 1000);
		String oauthTimestamp = seconds.toString();
		String nonce = getAlphaNumericString(6);

		oAuth1RequestParams.add("oauth_consumer_key=" + '"' + oAuth1.getConsumerKey() + '"');
		oAuth1RequestParams.add("oauth_nonce=" + '"' + nonce + '"');
		oAuth1RequestParams.add("oauth_signature_method=" + '"' + oAuth1.getSignatureMethod() + '"');
		oAuth1RequestParams.add("oauth_version=" + '"' + oAuth1.getVersion() + '"');

		//String key = getkey(oAuth1.getTokenSecret(), oAuth1.getConsumerSecret());
		String data = getOAuthSignature(methodType, apiUrl, oAuth1.getToken(), oAuth1.getConsumerKey(), oAuth1.getSignatureMethod(), oauthTimestamp, nonce, oAuth1.getVersion());
		//String signature = HmacSha1Signature.calculateRFC2104HMAC(data, key);
		String signature = getSignature(data, oAuth1.getTokenSecret(),  oAuth1.getConsumerSecret(),oAuth1.getSignatureMethod());
		oAuth1RequestParams.add("oauth_signature=" + '"' + signature + '"');
		oAuth1RequestParams.add("oauth_timestamp=" + '"' + oauthTimestamp + '"');

		headers.remove("Authorization");
		oAuth1RequestParams.add("oauth_token=" + '"' + oAuth1.getToken() + '"');
		headers.add("Authorization", "OAuth " + oAuth1RequestParams.toString());

	}
	 public static String getSignature(String baseString, String apiSecret, String tokenSecret,String signatureMethod )
	  {
	    try
	    {
	      Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
	      Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
	      return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret),signatureMethod);
	    } 
	    catch (Exception e)
	    {
	      throw new OAuthSignatureException(baseString, e);
	    }
	  }

	  private static String doSign(String toSign, String keyString,String signatureMethod) throws Exception
	  {
	    SecretKeySpec key = new SecretKeySpec((keyString).getBytes(WebServiceConstants.UTF8_CHARSET), WebServiceConstants.HMAC_SHA1);
	    Mac mac = Mac.getInstance(WebServiceConstants.HMAC_SHA1);
	    mac.init(key);
	    byte[] bytes = mac.doFinal(toSign.getBytes(WebServiceConstants.UTF8_CHARSET));
	    return bytesToBase64String(bytes).replace(WebServiceConstants.CARRIAGE_RETURN,WebServiceConstants.EMPTY_STRING);
	  }

	  private static String bytesToBase64String(byte[] bytes)
	  {
	    return Base64Encoder.getInstance().encode(bytes);
	  }
	static String getOAuthSignature(String methodType, String url, String accessToken, String oauthConsumerKey, String signatureMethod, String timeStamp, String oauthNonce, String oauthVersion) throws Exception
	{
		StringJoiner urlString = new StringJoiner("&");
		// urlString.add(methodType);
		urlString.add(url);
		urlString.add("oauth_nonce=" + oauthNonce);
		urlString.add("oauth_signature_method=" + signatureMethod);
		urlString.add("oauth_version=" + oauthVersion);
		urlString.add("oauth_consumer_key=" + oauthConsumerKey);
		urlString.add("oauth_timestamp=" + timeStamp);
		urlString.add("oauth_token=" + accessToken);
		String encodeURL = encode(urlString.toString());
		return methodType + "&" + encodeURL;
	}

	static String getkey(String tokenSecret, String consumerSecret) throws UnsupportedEncodingException
	{
		String encodeTokenSecret = URLEncoder.encode(tokenSecret, WebServiceConstants.UTF8_CHARSET);
		String encodeConsumerSecret = URLEncoder.encode(consumerSecret, WebServiceConstants.UTF8_CHARSET);
		return encodeTokenSecret + "&" + encodeConsumerSecret;
	}

	public static String encode(String url) throws Exception
	{
		try
		{
			String encodeURL = URLEncoder.encode(url, WebServiceConstants.UTF8_CHARSET);
			return encodeURL;
		}
		catch ( UnsupportedEncodingException e )
		{
			throw new Exception("Issue while encoding" + e.getMessage());
		}
	}

	public static OAuth1 getOAuth1RequestToken(WebServiceTemplateMaster webServiceTemplateMaster)
	{
		String requestUrl = null;
		String tokenURL = null;
		String authURL = null;
		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();
		if( webServiceTemplateMaster.isBaseUrlRequired() )
		{
			requestUrl = webServiceTemplateMaster.getBaseUrl() + oAuth1.getRequestURL();
			tokenURL = webServiceTemplateMaster.getBaseUrl() + oAuth1.getTokenURL();
			authURL = webServiceTemplateMaster.getBaseUrl() + oAuth1.getAuthURL() + "?oauth_token=";
		}
		else
		{
			requestUrl = oAuth1.getRequestURL();
			tokenURL = oAuth1.getTokenURL();
			authURL = oAuth1.getAuthURL();
		}
		MagentoThreeLeggedOAuth1 MagentoThreeLeggedOAuth1 = new MagentoThreeLeggedOAuth1(requestUrl, tokenURL, authURL);
		MagentoServiceOAuth1 magentoServiceOAuth1 = new MagentoServiceOAuth1(oAuth1.getConsumerKey(), oAuth1.getConsumerSecret(), oAuth1.getCallbackURL(), oAuth1.getScope(),MagentoThreeLeggedOAuth1);

		OAuthService oAuthService = magentoServiceOAuth1.getOAuthService();
		Token requestToken = magentoServiceOAuth1.getRequestToken(oAuthService);

		String authorizationUrl = magentoServiceOAuth1.getAuthorizationUrl(oAuthService, requestToken);

		oAuth1.setRequestToken(requestToken.getToken());
		oAuth1.setRequestTokenSecret(requestToken.getSecret());
		oAuth1.setAuthURL(authorizationUrl);
		return oAuth1;
	}

	public static String getAlphaNumericString(int n)
	{
		byte[] array = new byte[256];
		new Random().nextBytes(array);
		String randomString = new String(array, Charset.forName(WebServiceConstants.UTF8_CHARSET));
		StringBuffer r = new StringBuffer();
		String AlphaNumericString = randomString.replaceAll("[^A-Za-z0-9]", "");
		for (int k = 0; k < AlphaNumericString.length(); k++)
		{
			if( Character.isLetter(AlphaNumericString.charAt(k)) && (n > 0) || Character.isDigit(AlphaNumericString.charAt(k)) && (n > 0) )
			{
				r.append(AlphaNumericString.charAt(k));
				n--;
			}
		}
		return r.toString();
	}

	static Response getOAuth1Response(String url, WebServiceApi webServiceApi)
	{
		WebServiceTemplateMaster webServiceTemplateMaster = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster();
		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		MagentoThreeLeggedOAuth1 MagentoThreeLeggedOAuth1 = new MagentoThreeLeggedOAuth1(oAuth1.getRequestURL(), oAuth1.getTokenURL(), oAuth1.getAuthURL());
		MagentoServiceOAuth1 magentoServiceOAuth1 = new MagentoServiceOAuth1(oAuth1.getConsumerKey(), oAuth1.getConsumerSecret(), oAuth1.getCallbackURL(),oAuth1.getScope(), MagentoThreeLeggedOAuth1);

		OAuthService oAuthService = magentoServiceOAuth1.getOAuthService();
		OAuthRequest request = new OAuthRequest(Verb.GET, url);

		Token accessToken = new Token(oAuth1.getToken(), oAuth1.getTokenSecret());
		oAuthService.signRequest(accessToken, request);
		
		Response response = request.send();
		return response;
	}
	
	static Response postOAuth1Response(String url,WebServiceApi webServiceApi,JSONObject postBodyObject)
	{
		WebServiceTemplateMaster webServiceTemplateMaster = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster();
		OAuth1 oAuth1 = webServiceTemplateMaster.getoAuth1();

		MagentoThreeLeggedOAuth1 MagentoThreeLeggedOAuth1 = new MagentoThreeLeggedOAuth1(oAuth1.getRequestURL(), oAuth1.getTokenURL(), oAuth1.getAuthURL());
		MagentoServiceOAuth1 magentoServiceOAuth1 = new MagentoServiceOAuth1(oAuth1.getConsumerKey(), oAuth1.getConsumerSecret(), oAuth1.getCallbackURL(),oAuth1.getScope(), MagentoThreeLeggedOAuth1);

		OAuthService oAuthService = magentoServiceOAuth1.getOAuthService();
		OAuthRequest request =  new OAuthRequest(Verb.POST, url);

		Token accessToken = new Token(oAuth1.getToken(), oAuth1.getTokenSecret());
		oAuthService.signRequest(accessToken, request);
		
		Iterator<String> requiredBodyParamsJObjInterator = (Iterator<String>) postBodyObject.keySet().iterator();
		while (requiredBodyParamsJObjInterator.hasNext())
		{
			String paramKey = requiredBodyParamsJObjInterator.next();
			String paramValue = postBodyObject.getString(paramKey);
			request.addBodyParameter(paramKey, paramValue);
		}
		
		Response response = request.send();
		return response;
	}
}
