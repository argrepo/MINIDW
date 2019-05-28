package com.datamodel.anvizent.helper;

public class WebServiceConstants
{

	public static final String ACCESS_TOKEN = "access_token"; 
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CODE = "code";
	public static final String CALLER = "caller";
	public static final String TOKEN = "token";
	public static final String APPROVAL_PROMPT_KEY = "approval_prompt_key";
	public static final String ACCESS_TYPE_KEY = "access_type_key";
	public static final String REDIRECT_URI = "redirect_uri";
	public static final String RESPONSE_TYPE = "response_type";
	public static final String APPROVAL_PROMPT_VALUE = "approval_prompt_value";
	public static final String ACCESS_TYPE_VALUE = "access_type_value";
	public static final String AUTHENTICATION_SERVER_URL = "authentication_server_url";
	public static final String TOKEN_ENDPOINT_URL = "token_endpoint_url";
	public static final String CONFIG_FILE_PATH = "com/ibm/oauth/Oauth2Client.config";
	public static final String RESOURCE_SERVER_URL = "resource_server_url";
	public static final String GRANT_TYPE = "grant_type";
	public static final String GRANT_TYPE_PASSWORD = "password";
	public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
	public static final String SCOPE = "scope";
	public static final String LOCATION_HEADER = "Location";
	public static final String STATE = "state";
	public static final String AUTHORIZATION = "Authorization"; 
	public static final String BEARER = "Bearer";
	public static final String BASIC = "Basic"; 
	public static final String JSON_CONTENT = "application/json";  
	public static final String XML_CONTENT = "application/xml";
	public static final String URL_ENCODED_CONTENT = "application/x-www-form-urlencoded";
	public static final String HTTP_401_UNAUTHORIZED = "401 Unauthorized";
	public static final int HTTP_STATUS_OK = 200;
	public static String HTTP_GET="GET";
	public static String HTTP_POST="post";
	public static String HTTP_METHOD_GET="GET";
	public static String HTTP_METHOD_POST="POST";
	 
	public enum AuthenticationType { NO_AUTHENTICATION(1), AUTHENTICATION(2), BASIC_AUTHENTICATION(3), OAUTH1(4),OAUTH2(5),SESSION_BASED_AUTHENTICATION(6);
		public int type;  
		private AuthenticationType(int type){  
		this.type=type;  
		}  
	}
	  
	public enum PaginationType { OFFSET("offset"), DATE("date"),PAGE("page"), INCREMENTAL_DATE("incrementaldate"), HYPERMEDIA("hypermedia"),CONDITIONAL_DATE("conditionaldate");
		public String type;  
		private PaginationType(String type){   
		this.type=type;  
		}  
	}
	
	public enum PaginationParamType  { REQUEST_PARAMETER("Request Parameter"), BODY_PARAMETER("Body Parameter");
		public String type;  
		private PaginationParamType(String type){   
		this.type=type;  
		}  
	}
	
	public static final String INCREMENTAL_DATE = "1970-01-01 00:00:00 UTC";
	public static final String INCREMENTAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
	public static final String EPOCH_DATE_FORMAT = "epoch";
	public static final String TODAY_DATE_FORMAT = "yyyy-MM-dd";
	
	
	
	public enum WebserviceType  { SOAP("SOAP"), REST("REST");
		public String type;  
		private WebserviceType(String type){   
		this.type=type;  
		}  
	}
	
	public static final String HTTP_OK = "200";
	public static final String HTTP_FORBIDDEN = "403";
	public static final String HTTP_UNAUTHORIZED = "401"; 
	public static final String HTTP_SEND_REDIRECT = "302";
	public static final String HTTP_REQUEST_TIMEOUT = "408"; 
	public static final String HTTP_TOO_MANY_REQUESTS = "429"; 
	public static final String HTTP_INTERNAL_SERVER_ERROR = "500"; 
	public static final String HTTP_SERVICE_UNAVAILABLE = "503"; 
	public static final String HTTP_NOT_FOUND = "404"; 
 
	
	public static final String FILETYPE_CSV = "csv"; 
	public static final String FILETYPE_JSON = "json"; 
	public static final String FILETYPE_XML = "xml"; 
	
    public static final String UTF8_CHARSET = "UTF-8";
    public static final String EMPTY_STRING = "";
    public static final String CARRIAGE_RETURN = "\r\n";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String METHOD = "HMAC-SHA1";
    
	public static void main(String[] args)
	{
		System.out.println(WebServiceConstants.AuthenticationType.SESSION_BASED_AUTHENTICATION.type);
	}
 
}
